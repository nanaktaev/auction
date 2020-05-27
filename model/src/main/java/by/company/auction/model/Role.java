package by.company.auction.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN,
    USER,
    VENDOR;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
