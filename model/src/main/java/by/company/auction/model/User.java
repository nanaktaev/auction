package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;

@TableName("Users")
public class User extends BaseEntity {

    private String email;
    private String password;
    private String username;
    private Role role;
    private Integer companyId;

    @Override
    public User buildFromResultSet(ResultSet resultSet) throws SQLException {

        User user = new User();
        user.setId(resultSet.getInt(1));
        user.setEmail(resultSet.getString(2));
        user.setPassword(resultSet.getString(3));
        user.setUsername(resultSet.getString(4));
        user.setRole(Role.valueOf(resultSet.getString(5)));
        user.setCompanyId(resultSet.getInt(6));

        return user;
    }

    @Override
    public String toString() {
        return "Пользователь №" + super.getId() +
                ":\nemail - " + email +
                ", пароль - " + password +
                "\nимя - " + username +
                ", роль - " + role +
                "\nid компании - " + companyId +
                '.';
    }

    public Integer getId() {
        return super.getId();
    }

    public void setId(Integer id) {
        super.setId(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

}
