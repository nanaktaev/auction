package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.EntityAlreadyExistsException;
import by.company.auction.dto.UserDto;
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

    private static UserDto userDto;
    private static UserDto userDtoIncorrect;

    @BeforeClass
    public static void beforeAllTests() {

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setEmail("user@mail.com");
        userDto.setUsername("username");
        userDto.setPassword("password");

        userDtoIncorrect = new UserDto();
        userDtoIncorrect.setId(2);
        userDtoIncorrect.setEmail("user@mail.com");
        userDtoIncorrect.setUsername("username");
    }

    @Test
    public void validateCreationSuccess() {

        when(userService.findUserByEmail("user@mail.com")).thenReturn(null);
        when(userService.findUserByUsername("username")).thenReturn(null);

        userValidator.validate(userDto);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void validateCreationEmailExists() {

        when(userService.findUserByEmail("user@mail.com")).thenReturn(userDto);

        userValidator.validate(userDto);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void validateCreationUsernameExists() {

        when(userService.findUserByEmail("user@mail.com")).thenReturn(null);
        when(userService.findUserByUsername("username")).thenReturn(userDto);

        userValidator.validate(userDto);
    }

    @Test(expected = NullPointerException.class)
    public void validateCreationPropertyNotSet() {

        userValidator.validate(userDtoIncorrect);
    }

    @Test
    public void validateUpdateSuccess() {

        when(userService.findUserByEmail("user@mail.com")).thenReturn(userDto);
        when(userService.findUserByUsername("username")).thenReturn(userDto);

        userValidator.validateUpdate(userDto);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void validateUpdateEmailExists() {

        when(userService.findUserByEmail("user@mail.com")).thenReturn(userDtoIncorrect);

        userValidator.validateUpdate(userDto);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void validateUpdateUsernameExists() {

        when(userService.findUserByEmail("user@mail.com")).thenReturn(userDto);
        when(userService.findUserByUsername("username")).thenReturn(userDtoIncorrect);

        userValidator.validateUpdate(userDto);
    }
}