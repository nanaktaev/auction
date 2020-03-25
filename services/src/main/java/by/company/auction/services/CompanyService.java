package by.company.auction.services;

import by.company.auction.dao.CompanyDao;
import by.company.auction.model.Company;

public class CompanyService extends AbstractService<Company, CompanyDao> {

    private static CompanyService companyServiceInstance;

    private CompanyService() {
    }

    public static CompanyService getInstance() {
        if (companyServiceInstance != null) return companyServiceInstance;
        companyServiceInstance = new CompanyService();
        companyServiceInstance.setDao(CompanyDao.getInstance());
        return companyServiceInstance;
    }
}
