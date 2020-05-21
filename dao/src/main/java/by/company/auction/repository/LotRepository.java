package by.company.auction.repository;

import by.company.auction.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotRepository extends JpaRepository<Lot, Integer> {

    List<Lot> findLotsByTownId(Integer townId);

    List<Lot> findLotsByCategoryId(Integer categoryId);

    @Query(value = "SELECT lots.* FROM lots LEFT JOIN bids ON lots.id = bids.lot_id " +
            "WHERE bids.user_id = :userId GROUP BY lots.id",
            nativeQuery = true)
    List<Lot> findLotsByUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT lots.* FROM lots LEFT JOIN bids ON lots.id = bids.lot_id " +
            "WHERE bids.user_id = :userId AND lots.closes > NOW() GROUP BY lots.id",
            nativeQuery = true)
    List<Lot> findExpiredLotsByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query(value = "DELETE FROM bids WHERE lot_id = :lotId ;" +
            "DELETE FROM messages WHERE lot_id = :lotId ;" +
            "DELETE FROM lots WHERE id = :lotId", nativeQuery = true)
    void deleteLotWithBidsById(@Param("lotId") Integer id);

}
