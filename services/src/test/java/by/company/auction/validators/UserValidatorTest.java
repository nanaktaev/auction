package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.model.User;
import by.company.auction.services.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class UserValidatorTest extends AbstractTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserValidator userValidator;

    private static User user;

    @BeforeClass
    public static void beforeAllTests() {

        user = new User();
        user.setEmail("user@mail.com");
        user.setUsername("username");

    }

    @Test
    public void validateSuccess() {

        when(userService.findUserByEmail("user@mail.com")).thenReturn(null);
        when(userService.findUserByUsername("username")).thenReturn(null);

        userValidator.validate(user);

    }

    @Test(expected = AlreadyExistsException.class)
    public void validateEmailExists() {

        when(userService.findUserByEmail("user@mail.com")).thenReturn(user);

        userValidator.validate(user);

    }

    @Test(expected = AlreadyExistsException.class)
    public void validateUsernameExists() {

        when(userService.findUserByEmail("user@mail.com")).thenReturn(null);
        when(userService.findUserByUsername("username")).thenReturn(user);

        userValidator.validate(user);

    }

}