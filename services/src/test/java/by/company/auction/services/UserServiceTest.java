package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.converters.UserConverter;
import by.company.auction.dto.UserDto;
import by.company.auction.model.Role;
import by.company.auction.model.User;
import by.company.auction.repository.UserRepository;
import by.company.auction.security.SecurityValidator;
import by.company.auction.validators.UserValidator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest extends AbstractTest {

    @SuppressWarnings("unused")
    @Spy
    private UserConverter userConverter;
    @SuppressWarnings("unused")
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private SecurityValidator securityValidator;
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private static User user;
    private static UserDto userDto;
    private final ArgumentCaptor<User> CAPTOR = ArgumentCaptor.forClass(User.class);

    @BeforeClass
    public static void beforeAllTests() {

        userDto = new UserDto();
        userDto.setEmail("admin@mail.com");
        userDto.setUsername("admin");
        userDto.setPassword("admin");

        user = new User();
        user.setId(1);
        user.setEmail("user@mail.com");
        user.setUsername("user");
        user.setPassword("user");
        user.setRole(Role.USER);
    }

    @Test
    public void registerOrdinaryUser() {

        when(userRepository.isUserRepositoryEmpty()).thenReturn(false);
        doNothing().when(userValidator).validate(userDto);
        when(userRepository.save(any())).thenReturn(user);

        userService.registerUser(userDto);

        verify(userRepository).save(CAPTOR.capture());
        User registeredUser = CAPTOR.getValue();

        assertEquals(Role.USER, registeredUser.getRole());
    }

    @Test
    public void registerFirstUser() {

        when(userRepository.isUserRepositoryEmpty()).thenReturn(true);
        doNothing().when(userValidator).validate(userDto);
        when(userRepository.save(any())).thenReturn(user);

        userService.registerUser(userDto);

        verify(userRepository).save(CAPTOR.capture());
        User registeredUser = CAPTOR.getValue();

        assertEquals(Role.ADMIN, registeredUser.getRole());
    }

    @Test
    public void findUserByUsername() {

        when(userRepository.findUserByUsername("user")).thenReturn(user);

        UserDto receivedUser = userService.findUserByUsername("user");

        assertEquals(receivedUser.getUsername(), "user");
    }

    @Test
    public void findUserByEmail() {

        when(userRepository.findUserByEmail("user@mail.com")).thenReturn(user);

        UserDto receivedUser = userService.findUserByEmail("user@mail.com");

        assertEquals(receivedUser.getEmail(), "user@mail.com");
    }

    @Test
    public void updateUserSuccess() {

        UserDto userDtoFromRequestBody = new UserDto();
        userDtoFromRequestBody.setId(1);
        userDtoFromRequestBody.setRole(Role.VENDOR);

        doNothing().when(securityValidator).validateUserAccountOwnership(1);
        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user));
        when(securityValidator.isCurrentUserAdmin()).thenReturn(true);
        doNothing().when(userValidator).validateUpdate(any());
        when(userRepository.save(any())).then(returnsFirstArg());

        UserDto updatedUser = userService.update(userDtoFromRequestBody);

        assertEquals(Role.VENDOR, updatedUser.getRole());
    }

    @Test
    public void updateUserNotEnoughPrivileges() {

        UserDto userDtoFromRequestBody = new UserDto();
        userDtoFromRequestBody.setId(1);
        userDtoFromRequestBody.setRole(Role.VENDOR);

        doNothing().when(securityValidator).validateUserAccountOwnership(1);
        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user));
        when(securityValidator.isCurrentUserAdmin()).thenReturn(false);
        doNothing().when(userValidator).validateUpdate(any());
        when(userRepository.save(any())).thenReturn(user);

        UserDto updatedUser = userService.update(userDtoFromRequestBody);

        assertEquals(Role.USER, updatedUser.getRole());
    }
}