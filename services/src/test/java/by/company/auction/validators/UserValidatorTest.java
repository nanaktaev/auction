package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.model.User;
import by.company.auction.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserValidatorTest extends AbstractTest {

    private UserService userService;
    private UserValidator userValidator;
    private User user;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(UserService.class);
        PowerMockito.when(UserService.getInstance()).thenReturn(mock(UserService.class));
        MockitoAnnotations.initMocks(this);

        userValidator = UserValidator.getInstance();
        userService = UserService.getInstance();

        user = new User();
        user.setEmail("user@mail.com");
        user.setUsername("user");

    }

    @Test
    @PrepareForTest({UserService.class, UserValidator.class})
    public void validateSuccess() {

        when(userService.findUserByEmail(anyString())).thenReturn(null);
        when(userService.findUserByUsername(anyString())).thenReturn(null);

        userValidator.validate(user);

    }

    @Test(expected = AlreadyExistsException.class)
    @PrepareForTest({UserService.class, UserValidator.class})
    public void validateEmailExists() {

        when(userService.findUserByEmail(anyString())).thenReturn(user);
        when(userService.findUserByUsername(anyString())).thenReturn(null);

        userValidator.validate(user);

    }

    @Test(expected = AlreadyExistsException.class)
    @PrepareForTest({UserService.class, UserValidator.class})
    public void validateUsernameExists() {

        when(userService.findUserByEmail(anyString())).thenReturn(null);
        when(userService.findUserByUsername(anyString())).thenReturn(user);

        userValidator.validate(user);

    }

}