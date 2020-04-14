package by.company.auction.services;

import by.company.auction.dao.UserDao;
import by.company.auction.model.Role;
import by.company.auction.model.User;
import by.company.auction.validators.UserValidator;

import org.apache.commons.collections4.CollectionUtils;

public class UserService extends AbstractService<User, UserDao> {

    private static UserService userServiceInstance;

    private UserService() {
    }

    public User updateUserRole(Integer id, String roleString, Integer companyId) {
        if (companyId != null && !TownService.getInstance().exists(companyId)) {
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

        UserValidator.getInstance().validate(user);

        user.setRole(CollectionUtils.isEmpty(findAll()) ? Role.ADMIN : Role.USER);

        return create(user);
    }

    public static UserService getInstance() {
        if (userServiceInstance != null) {
            return userServiceInstance;
        }
        userServiceInstance = new UserService();
        userServiceInstance.setDao(UserDao.getInstance());
        return userServiceInstance;
    }

}