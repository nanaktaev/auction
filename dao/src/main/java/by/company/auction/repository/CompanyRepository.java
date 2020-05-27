package by.company.auction.repository;

import by.company.auction.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findCompanyByName(String name);

    @Query(value = "SELECT CASE WHEN EXISTS(SELECT * FROM lots WHERE company_id = :companyId) THEN false ELSE true END",
            nativeQuery = true)
    boolean isCompanyEmptyOfLots(@Param("companyId") Integer companyId);

    @Query(value = "SELECT CASE WHEN EXISTS(SELECT * FROM users WHERE company_id = :companyId) THEN false ELSE true END",
            nativeQuery = true)
    boolean isCompanyEmptyOfUsers(@Param("companyId") Integer companyId);
}
