package by.company.auction.validators;

import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.model.Company;
import by.company.auction.services.CompanyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CompanyValidator {

    @Autowired
    private CompanyService companyService;

    public void validate(Company company) {

        log.debug("validate() company = {}", company);

        if (companyService.findCompanyByName(company.getName()) != null) {
            throw new AlreadyExistsException("Компания с таким именем уже была добавлена.");
        }
    }

}
