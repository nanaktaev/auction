package by.company.auction.services;

import by.company.auction.common.exceptions.WrongCredentialsException;
import by.company.auction.dto.UserPrincipalAuction;
import by.company.auction.model.User;
import by.company.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceAuction implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws WrongCredentialsException {

        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new WrongCredentialsException("Credentials are incorrect.");
        }
        return new UserPrincipalAuction(user);
    }
}
