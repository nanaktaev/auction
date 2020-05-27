package by.company.auction.validators;

import by.company.auction.common.exceptions.EntityAlreadyExistsException;
import by.company.auction.dto.CompanyDto;
import by.company.auction.services.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompanyValidator {

    @Autowired
    private CompanyService companyService;

    public void validate(CompanyDto companyDto) {

        log.debug("validate() companyDto = {}", companyDto);

        if (companyService.findCompanyByName(companyDto.getName()) != null) {
            throw new EntityAlreadyExistsException("This company has already been added.");
        }
    }
}
