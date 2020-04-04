package by.company.auction.secuirty;

import by.company.auction.model.Role;
import by.company.auction.model.User;

public class Authentication {
    private Integer userId;
    private String username;
    private Role userRole;
    private Integer userCompanyId;

    Authentication() {
    }

    public void authenticateUser(User user) {
        userId = user.getId();
        username = user.getUsername();
        userRole = user.getRole();
        userCompanyId = user.getCompanyId();
    }

    public void clear() {
        userId = null;
        username = null;
        userRole = null;
        userCompanyId = null;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public Integer getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(Integer userCompanyId) {
        this.userCompanyId = userCompanyId;
    }
}
