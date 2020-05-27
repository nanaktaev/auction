package by.company.auction.dto;

import by.company.auction.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto {

    @Email
    private String email;

    @Length(min = 5)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Length(min = 5)
    @Pattern(regexp = "[0-9a-zA-Z\u0400-\u04ff -]{3,30}")
    private String username;

    private Role role;

    private CompanyDto company;
}
