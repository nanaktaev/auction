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

    public User(Integer id, String email, String password, String username, Integer companyId) {
        this.setId(id);
        this.email = email;
        this.password = password;
        this.username = username;
        this.companyId = companyId;
    }

    @Override
    public User buildFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt(1);
            String email = resultSet.getString(2);
            String password = resultSet.getString(3);
            String username = resultSet.getString(4);
            int companyId = resultSet.getInt(5);

            return new User(id, email, password, username, companyId);

        } catch (
                SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "Пользователь №" + super.toString() +
                ":\nemail - " + email +
                ", пароль - " + password +
                "\nимя - " + username +
                ", роль - " + role +
                "\nid компании - " + companyId +
                '.';
    }

    public User(String email, String password, String username, Role role, Integer companyId) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.companyId = companyId;
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
