package validators;

import by.company.auction.model.User;
import by.company.auction.services.UserService;

public class UserValidator {

    public static void validate(User user) {

        UserService userService = UserService.getInstance();

        if (userService.findUserByEmail(user.getEmail()) != null) {
            throw new IllegalStateException("Ошибка. Данная почта уже используется.");
        }
        if (userService.findUserByUsername(user.getUsername()) != null) {
            throw new IllegalStateException("Ошибка. Данное имя пользователя уже занято.");
        }
    }
}
