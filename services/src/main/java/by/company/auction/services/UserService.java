package by.company.auction.services;

import by.company.auction.dao.UserDao;
import by.company.auction.model.Role;
import by.company.auction.model.User;
import validators.UserValidator;

import java.util.ArrayList;

public class UserService extends AbstractService<User, UserDao> {

    private static UserService userServiceInstance;

    private UserService() {
    }

    public User updateUserRole(Integer id, String roleString, Integer companyId) {
        if (companyId != null && !CompanyService.getInstance().exists(companyId)) {
            throw new IllegalStateException("Ошибка. Компания по данному id не найдена.");
        }
        User user = findById(id);
        user.setRole(Role.valueOf(roleString.toUpperCase()));
        user.setCompanyId(companyId);

        return update(user);
    }

    public User findUserByEmail(String email) {
        return dao.findUserByEmail(email);
    }

    public User findUserByUsername(String username) {
        return dao.findUserByUsername(username);
    }

    public User registerUser(User user) {

        UserValidator.validate(user);

        user.setRole(Role.USER);
        user.setBidIds(new ArrayList<>());
        user.setMessageIds(new ArrayList<>());
        user.setLotIds(new ArrayList<>());

        return create(user);
    }

    public static UserService getInstance() {
        if (userServiceInstance != null) return userServiceInstance;
        userServiceInstance = new UserService();
        userServiceInstance.setDao(UserDao.getInstance());
        return userServiceInstance;
    }
}
