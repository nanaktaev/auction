package by.company.auction.dto;

import by.company.auction.model.Role;
import by.company.auction.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipalAuction implements UserDetails {

    private User user;

    public UserPrincipalAuction(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(this.user.getRole());
    }

    public Integer getId() {
        return this.user.getId();
    }

    public Integer getCompanyId() {
        return this.user.getCompany().getId();
    }

    public Role getRole() {
        return this.user.getRole();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
