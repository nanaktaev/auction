package by.company.auction.security;

import by.company.auction.common.exceptions.OwnershipException;
import by.company.auction.dto.UserPrincipalAuction;
import by.company.auction.model.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityValidator {

    public UserPrincipalAuction getUserPrincipal() {

        return (UserPrincipalAuction)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void validateCompanyAffiliation(Integer companyId) {

        UserPrincipalAuction principal = getUserPrincipal();

        if (!isCurrentUserAdmin() && !principal.getCompanyId().equals(companyId)) {
            throw new OwnershipException("You can only edit/delete your own lots.");
        }
    }

    public void validateUserAccountOwnership(Integer userId) {

        UserPrincipalAuction principal = getUserPrincipal();

        if (!isCurrentUserAdmin() && !principal.getId().equals(userId)) {
            throw new OwnershipException("You can only edit/view your own user account.");
        }
    }

    public boolean isCurrentUserAdmin() {

        UserPrincipalAuction principal = getUserPrincipal();

        return (principal.getRole() == Role.ADMIN);
    }
}
