package by.company.auction.validators;

import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.model.User;
import by.company.auction.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserValidator {

    private final Logger LOGGER = LogManager.getLogger(UserValidator.class);

    public void validate(User user) {

        LOGGER.debug("validate() user = {}", user);

        UserService userService = UserService.getInstance();

        if (userService.findUserByEmail(user.getEmail()) != null) {
            throw new AlreadyExistsException("Ошибка. Данная почта уже используется.");
        }
        if (userService.findUserByUsername(user.getUsername()) != null) {
            throw new AlreadyExistsException("Ошибка. Данное имя пользователя уже занято.");
        }
    }

    private static UserValidator userValidatorInstance;

    public static UserValidator getInstance() {
        if (userValidatorInstance != null) {
            return userValidatorInstance;
        }
        userValidatorInstance = new UserValidator();
        return userValidatorInstance;
    }

}
