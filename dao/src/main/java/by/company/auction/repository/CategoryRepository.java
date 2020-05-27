package by.company.auction.repository;

import by.company.auction.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findCategoryByName(String name);

    @Query(value = "SELECT CASE WHEN EXISTS(SELECT * FROM lots WHERE category_id = :categoryId) THEN false ELSE true END",
            nativeQuery = true)
    boolean isCategoryEmptyOfLots(@Param("categoryId") Integer categoryId);
}
