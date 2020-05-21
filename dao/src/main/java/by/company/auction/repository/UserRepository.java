package by.company.auction.repository;

import by.company.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    @Query(value = "SELECT CASE WHEN EXISTS(SELECT * FROM users) THEN false ELSE true END",
            nativeQuery = true)
    boolean isUserRepositoryEmpty();
}
