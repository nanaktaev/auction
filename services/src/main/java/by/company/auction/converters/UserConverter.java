package by.company.auction.converters;

import by.company.auction.dto.UserDto;
import by.company.auction.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends AbstractConverter<User, UserDto> {

    @Autowired
    private CompanyConverter companyConverter;

    @Override
    public UserDto convertToDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole());
        if (user.getCompany() != null) {
            userDto.setCompany(companyConverter.convertToDto(user.getCompany()));
        }
        return userDto;
    }

    @Override
    public User convertToEntity(UserDto userDto) {

        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        user.setRole(userDto.getRole());
        if (userDto.getCompany() != null) {
            user.setCompany(companyConverter.convertToEntity(userDto.getCompany()));
        }
        return user;
    }
}
