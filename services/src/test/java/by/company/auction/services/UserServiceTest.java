package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.model.Company;
import by.company.auction.model.Role;
import by.company.auction.model.User;
import by.company.auction.repository.UserRepository;
import by.company.auction.validators.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserServiceTest extends AbstractTest {

    @Mock
    private UserValidator userValidator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyService companyService;
    @InjectMocks
    private UserService userService;

    private User user;
    private User newUser;
    private Company company;

    @Before
    public void beforeEachTest() {

        newUser = new User();
        newUser.setEmail("admin@mail.com");
        newUser.setUsername("admin");
        newUser.setPassword("admin");

        user = new User();
        user.setId(1);
        user.setEmail("user@mail.com");
        user.setUsername("user");
        user.setPassword("user");
        user.setRole(Role.USER);

        company = new Company();
        company.setId(1);

    }

    @Test
    public void registerOrdinaryUser() {

        when(userRepository.isUserRepositoryEmpty()).thenReturn(false);
        doNothing().when(userValidator).validate(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);

        User registeredUser = userService.registerUser(newUser);

        assertEquals(Role.USER, registeredUser.getRole());

    }

    @Test
    public void registerFirstUser() {

        when(userRepository.isUserRepositoryEmpty()).thenReturn(true);
        doNothing().when(userValidator).validate(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);

        User registeredUser = userService.registerUser(newUser);

        assertEquals(Role.ADMIN, registeredUser.getRole());

    }

    @Test
    public void findUserByUsername() {

        when(userRepository.findUserByUsername("user")).thenReturn(user);

        User receivedUser = userService.findUserByUsername("user");

        assertNotNull(receivedUser);

    }

    @Test
    public void findUserByEmail() {

        when(userRepository.findUserByEmail("user@mail.com")).thenReturn(user);

        User receivedUser = userService.findUserByEmail("user@mail.com");

        assertNotNull(receivedUser);

    }

    @Test
    public void updateUserRole() {

        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user));
        when(companyService.findById(1)).thenReturn(company);
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateUserRole(user.getId(), "VENDOR", 1);

        assertEquals("VENDOR", updatedUser.getRole().name());
    }

    @Test(expected = NoSuchEntityException.class)
    public void updateUserRoleWhileCompanyIsAbsent() {

        when(userRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user));
        when(companyService.findById(1)).thenThrow(new NoSuchEntityException("Ничего не найдено."));

        User updatedUser = userService.updateUserRole(user.getId(), "VENDOR", 1);

        assertEquals("USER", updatedUser.getRole().name());

    }

}