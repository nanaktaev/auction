package by.company.auction.services;

import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.converters.UserConverter;
import by.company.auction.dto.UserDto;
import by.company.auction.dto.UserPrincipalAuction;
import by.company.auction.model.Role;
import by.company.auction.model.User;
import by.company.auction.repository.UserRepository;
import by.company.auction.security.SecurityValidator;
import by.company.auction.utils.CustomBeanUtils;
import by.company.auction.validators.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class UserService extends AbstractService<User, UserDto, UserRepository, UserConverter> {

    @Autowired
    private UserValidator userValidator;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private SecurityValidator securityValidator;

    protected UserService(UserRepository repository, UserConverter converter) {
        super(repository, converter);
    }

    public List<UserDto> findAll(Pageable pageable) {

        List<User> users = repository.findAll(pageable).getContent();

        if (users.isEmpty()) {
            throw new NotYetPopulatedException("There is nothing here yet.");
        }

        return converter.convertListToDto(users);
    }

    public UserDto findUserByEmail(String email) {

        log.debug("findUserByEmail() email = {}", email);

        User user = repository.findUserByEmail(email);

        if (user == null) {
            return null;
        }

        return converter.convertToDto(user);
    }

    public UserDto findUserByUsername(String username) {

        log.debug("findUserByUsername() username = {}", username);

        User user = repository.findUserByUsername(username);

        if (user == null) {
            return null;
        }

        return converter.convertToDto(user);
    }

    public UserDto registerUser(UserDto userDto) {

        log.debug("registerUser() userDto = {}", userDto);

        userValidator.validate(userDto);

        userDto.setRole(repository.isUserRepositoryEmpty() ? Role.ADMIN : Role.USER);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return create(userDto);
    }

    @Override
    public UserDto update(UserDto userDto) {

        log.debug("update() userDto = {}", userDto);

        securityValidator.validateUserAccountOwnership(userDto.getId());

        UserDto updatedUser = findById(userDto.getId());

        if (!securityValidator.isCurrentUserAdmin()) {
            userDto.setRole(null);
            userDto.setCompany(null);
        }

        CustomBeanUtils.copyNotNullProperties(userDto, updatedUser);

        userValidator.validateUpdate(updatedUser);

        return super.update(updatedUser);
    }

    public UserDto findCurrentUser() {

        UserPrincipalAuction principal = (UserPrincipalAuction)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return findById(principal.getId());
    }
}