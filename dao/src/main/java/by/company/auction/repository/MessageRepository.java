package by.company.auction.repository;

import by.company.auction.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findMessagesByUserId(Integer userId);

    @Query(value = "SELECT m FROM Message m WHERE m.user.id = :userId AND m.type = 'OUTCOME'")
    List<Message> findOutcomeMessagesByUserId(@Param("userId") Integer userId);

}
