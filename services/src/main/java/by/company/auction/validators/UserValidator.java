package by.company.auction.validators;

import by.company.auction.common.exceptions.EntityAlreadyExistsException;
import by.company.auction.dto.UserDto;
import by.company.auction.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserValidator {

    @Autowired
    private UserService userService;

    public void validate(UserDto userDto) {

        log.debug("validate() userDto = {}", userDto);

        validateNullProperties(userDto);

        if (userService.findUserByEmail(userDto.getEmail()) != null) {
            throw new EntityAlreadyExistsException("This email is already in use.");
        }
        if (userService.findUserByUsername(userDto.getUsername()) != null) {
            throw new EntityAlreadyExistsException("This username is already in use.");
        }
    }

    public void validateUpdate(UserDto userDto) {

        log.debug("validate() userDto = {}", userDto);

        UserDto existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && !existingUser.getId().equals(userDto.getId())) {
            throw new EntityAlreadyExistsException("This email is already in use.");
        }

        existingUser = userService.findUserByUsername(userDto.getUsername());

        if (existingUser != null && !existingUser.getId().equals(userDto.getId())) {
            throw new EntityAlreadyExistsException("This username is already in use.");
        }
    }

    private void validateNullProperties(UserDto userDto) {

        if (userDto.getPassword() == null) {
            throw new NullPointerException("Password is necessary.");
        }
        if (userDto.getEmail() == null) {
            throw new NullPointerException("Email is necessary.");
        }
        if (userDto.getUsername() == null) {
            throw new NullPointerException("Username is necessary.");
        }
    }
}
