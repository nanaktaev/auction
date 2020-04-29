package by.company.auction.validators;

import by.company.auction.exceptions.AuctionException;
import by.company.auction.model.User;
import by.company.auction.services.UserService;

public class UserValidator {

    public void validate(User user) {

        UserService userService = UserService.getInstance();

        if (userService.findUserByEmail(user.getEmail()) != null) {
            throw new AuctionException("Ошибка. Данная почта уже используется.");
        }
        if (userService.findUserByUsername(user.getUsername()) != null) {
            throw new AuctionException("Ошибка. Данное имя пользователя уже занято.");
        }
    }

    private static UserValidator userValidatorInstance;

    private UserValidator() {
    }

    public static UserValidator getInstance() {
        if (userValidatorInstance != null) {
            return userValidatorInstance;
        }
        userValidatorInstance = new UserValidator();
        return userValidatorInstance;
    }
}
