package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.NotFoundException;
import by.company.auction.dao.UserDao;
import by.company.auction.model.Role;
import by.company.auction.model.User;
import by.company.auction.validators.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class UserServiceTest extends AbstractTest {

    private User user;
    private User newUser;
    private List<User> users;
    private List<User> emptyUsers;
    private UserService userService;
    private UserValidator userValidator;
    private UserDao userDao;
    private TownService companyService;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(UserValidator.class);
        PowerMockito.when(UserValidator.getInstance()).thenReturn(mock(UserValidator.class));
        PowerMockito.mockStatic(UserDao.class);
        PowerMockito.when(UserDao.getInstance()).thenReturn(mock(UserDao.class));
        PowerMockito.mockStatic(TownService.class);
        PowerMockito.when(TownService.getInstance()).thenReturn(mock(TownService.class));
        MockitoAnnotations.initMocks(this);

        userService = UserService.getInstance();
        userValidator = UserValidator.getInstance();
        userDao = UserDao.getInstance();
        companyService = TownService.getInstance();

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

        users = Collections.singletonList(user);
        emptyUsers = Collections.emptyList();

    }

    @Test
    @PrepareForTest({UserService.class, UserValidator.class, UserDao.class, TownService.class})
    public void registerOrdinaryUser() {

        when(userDao.findAll()).thenReturn(users);
        when(userDao.create(any())).thenReturn(newUser);
        doNothing().when(userValidator).validate(newUser);

        User registeredUser = userService.registerUser(newUser);

        assertEquals(Role.USER, registeredUser.getRole());

    }

    @Test
    @PrepareForTest({UserService.class, UserValidator.class, UserDao.class, TownService.class})
    public void registerFirstUser() {

        when(userDao.findAll()).thenReturn(emptyUsers);
        when(userDao.create(any())).thenReturn(newUser);
        doNothing().when(userValidator).validate(newUser);

        User registeredUser = userService.registerUser(newUser);

        assertEquals(Role.ADMIN, registeredUser.getRole());

    }

    @Test
    @PrepareForTest({UserService.class, UserValidator.class, UserDao.class, TownService.class})
    public void findUserByUsername() {

        when(userDao.findUserByUsername(anyString())).thenReturn(user);

        User receivedUser = userService.findUserByUsername("username");

        assertNotNull(receivedUser);

    }

    @Test
    @PrepareForTest({UserService.class, UserValidator.class, UserDao.class, TownService.class})
    public void findUserByEmail() {

        when(userDao.findUserByEmail(anyString())).thenReturn(user);

        User receivedUser = userService.findUserByEmail("user@mail.com");

        assertNotNull(receivedUser);

    }

    @Test
    @PrepareForTest({UserService.class, UserValidator.class, UserDao.class, TownService.class})
    public void updateUserRole() {

        when(companyService.exists(anyInt())).thenReturn(true);
        when(userService.findById(anyInt())).thenReturn(user);
        when(userService.update(user)).thenReturn(user);

        User updatedUser = userService.updateUserRole(user.getId(), "VENDOR", 1);

        assertEquals("VENDOR", updatedUser.getRole().name());
    }

    @Test(expected = NotFoundException.class)
    @PrepareForTest({UserService.class, UserValidator.class, UserDao.class, TownService.class})
    public void updateUserRoleWhileCompanyIsAbsent() {

        when(companyService.exists(anyInt())).thenReturn(false);

        User updatedUser = userService.updateUserRole(user.getId(), "VENDOR", 1);

        assertEquals("USER", updatedUser.getRole().name());

    }

}