package by.company.auction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@SequenceGenerator(name = "id_generator", sequenceName = "user_sequence")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Length(min = 5)
    private String password;

    @NotEmpty
    @Length(min = 5)
    @Pattern(regexp = "[0-9a-zA-Z\u0400-\u04ff -]{3,30}")
    private String username;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Override
    public String toString() {
        return "Пользователь №" + getId() +
                ":\nemail - " + email +
                ", пароль - " + password +
                "\nимя - " + username +
                ", роль - " + role +
                "\nкомпания - " + company +
                ".\n";
    }

}
