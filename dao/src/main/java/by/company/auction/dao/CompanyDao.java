package by.company.auction.dao;

import by.company.auction.model.Company;

public class CompanyDao extends AbstractDao<Company> {

    private static CompanyDao companyDaoInstance;

    private CompanyDao() {
    }

    public static CompanyDao getInstance() {
        if (companyDaoInstance != null) return companyDaoInstance;
        companyDaoInstance = new CompanyDao();
        return companyDaoInstance;
    }
}
