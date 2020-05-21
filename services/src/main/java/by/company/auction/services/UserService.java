package by.company.auction.services;

import by.company.auction.model.Role;
import by.company.auction.model.User;
import by.company.auction.repository.UserRepository;
import by.company.auction.validators.UserValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
public class UserService extends AbstractService<User, UserRepository> {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserValidator userValidator;

    protected UserService(UserRepository repository) {
        super(repository);
    }

    public User updateUserRole(Integer id, String roleString, Integer companyId) {

        log.debug("updateUserRole() id = {}, roleString = {}, companyId = {}", id, roleString, companyId);

        User user = findById(id);

        if (companyId != null) {
            user.setCompany(companyService.findById(companyId));
        }

        user.setRole(Role.valueOf(roleString.toUpperCase()));

        return update(user);

    }

    public User findUserByEmail(String email) {

        log.debug("findUserByEmail() email = {}", email);
        return repository.findUserByEmail(email);

    }

    public User findUserByUsername(String username) {

        log.debug("findUserByUsername() username = {}", username);
        return repository.findUserByUsername(username);

    }

    public User registerUser(User user) {

        log.debug("registerUser() user = {}", user);

        userValidator.validate(user);

        user.setRole(repository.isUserRepositoryEmpty() ? Role.ADMIN : Role.USER);

        return create(user);
    }

}