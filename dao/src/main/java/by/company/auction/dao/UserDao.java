package by.company.auction.dao;

import by.company.auction.model.User;

public class UserDao extends AbstractDao<User> {

    private static UserDao userDaoInstance;

    private UserDao() {
    }

    public User findUserByEmail(String email) {
        for (User user : findAll()) {
            if (email.equals(user.getEmail())) {
                return user;
            }
        }
        return null;
    }

    public User findUserByUsername(String username) {
        for (User user : findAll()) {
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    public static UserDao getInstance() {
        if (userDaoInstance != null) {
            return userDaoInstance;
        }
        userDaoInstance = new UserDao();
        return userDaoInstance;
    }

}
