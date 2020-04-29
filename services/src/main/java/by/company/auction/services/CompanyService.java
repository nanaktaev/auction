package by.company.auction.services;

import by.company.auction.dao.CompanyDao;
import by.company.auction.model.Company;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompanyService extends AbstractService<Company, CompanyDao> {

    private static CompanyService companyServiceInstance;
    private final Logger LOGGER = LogManager.getLogger(CompanyService.class);

    public Company findCompanyByName(String name) {

        LOGGER.debug("findCompanyByName() name = {}", name);
        return dao.findCompanyByName(name);

    }

    public static CompanyService getInstance() {
        if (companyServiceInstance != null) {
            return companyServiceInstance;
        }
        companyServiceInstance = new CompanyService();
        companyServiceInstance.setDao(CompanyDao.getInstance());
        return companyServiceInstance;
    }
}
