package by.company.auction.validators;

import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.common.exceptions.WrongCredentialsException;
import by.company.auction.model.User;
import by.company.auction.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class UserValidator {

    @Autowired
    private UserService userService;

    public void validate(User user) {

        log.debug("validate() user = {}", user);

        if (userService.findUserByEmail(user.getEmail()) != null) {
            throw new AlreadyExistsException("Ошибка. Данная почта уже используется.");
        }
        if (userService.findUserByUsername(user.getUsername()) != null) {
            throw new AlreadyExistsException("Ошибка. Данное имя пользователя уже занято.");
        }
    }

    public User validateLogin(String email, String password) {

        log.debug("validateLogin() email = {}, password = {}", email, password);

        User user = userService.findUserByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            throw new WrongCredentialsException("Введенные данные не верны.");
        }

        return user;
    }

}
