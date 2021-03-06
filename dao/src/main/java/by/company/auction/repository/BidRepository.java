package by.company.auction.repository;

import by.company.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

    List<Bid> findBidsByLotId(Integer lotId);

    @Query(value = "SELECT * FROM bids WHERE lot_id = :lotId ORDER BY value DESC limit 1",
            nativeQuery = true)
    Bid findTopBidByLotId(@Param("lotId") Integer lotId);
}
