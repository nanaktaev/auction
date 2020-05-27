package by.company.auction.repository;

import by.company.auction.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TownRepository extends JpaRepository<Town, Integer> {

    Town findTownByName(String name);

    @Query(value = "SELECT CASE WHEN EXISTS(SELECT * FROM lots WHERE town_id = :townId) THEN false ELSE true END",
            nativeQuery = true)
    boolean isTownEmptyOfLots(@Param("townId") Integer townId);
}
