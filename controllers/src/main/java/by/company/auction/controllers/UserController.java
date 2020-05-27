package by.company.auction.controllers;

import by.company.auction.dto.UserDto;
import by.company.auction.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@PageableDefault(size = 8, sort = "id") Pageable pageable) {

        log.debug("getAllUsers()");

        return userService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Integer id) {

        log.debug("getUserById() id = {}", id);

        return userService.findById(id);
    }

    @GetMapping("/current")
    public UserDto getCurrentUser() {

        log.debug("getCurrentUser()");

        return userService.findCurrentUser();
    }

    @PostMapping
    public UserDto registerUser(@Valid @RequestBody UserDto userDto) {

        log.debug("create() userDto = {}", userDto);

        return userService.registerUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {

        log.debug("deleteUser() id = {}", id);

        userService.delete(id);
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable Integer id, @Valid @RequestBody UserDto userDto) {

        userDto.setId(id);

        log.debug("patchUser() userDto = {}", userDto);

        return userService.update(userDto);
    }

}
