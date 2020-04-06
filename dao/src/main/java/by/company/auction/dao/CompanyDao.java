package by.company.auction.dao;

import by.company.auction.model.Company;

public class CompanyDao extends AbstractDao<Company> {

    private static CompanyDao companyDaoInstance;

    private CompanyDao() {
        super(Company.class);
    }

    public Company findByName(String name) {
        for (Company company : findAll()) {
            if (name.equals(company.getName())) {
                return company;
            }
        }
        return null;
    }

    public static CompanyDao getInstance() {
        if (companyDaoInstance != null) {
            return companyDaoInstance;
        }
        companyDaoInstance = new CompanyDao();
        return companyDaoInstance;
    }
}
