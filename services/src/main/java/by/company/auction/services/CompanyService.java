package by.company.auction.services;

import by.company.auction.common.exceptions.ParentDeletionException;
import by.company.auction.converters.CompanyConverter;
import by.company.auction.dto.CompanyDto;
import by.company.auction.model.Company;
import by.company.auction.repository.CompanyRepository;
import by.company.auction.validators.CompanyValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CompanyService extends AbstractService<Company, CompanyDto, CompanyRepository, CompanyConverter> {

    @Autowired
    private CompanyValidator companyValidator;

    protected CompanyService(CompanyRepository repository, CompanyConverter converter) {
        super(repository, converter);
    }

    public CompanyDto findCompanyByName(String name) {

        log.debug("findCompanyByName() name = {}", name);

        Company company = repository.findCompanyByName(name);

        if (company == null) {
            return null;
        }

        return converter.convertToDto(company);
    }

    @Override
    public CompanyDto create(CompanyDto companyDto) {

        log.debug("create() companyDto = {}", companyDto);
        companyValidator.validate(companyDto);

        return super.create(companyDto);
    }

    @Override
    public void delete(Integer id) {

        log.debug("delete() companyId = {}", id);

        if (!repository.isCompanyEmptyOfLots(id)) {
            throw new ParentDeletionException("This company cannot be deleted because it still has lots.");
        }
        if (!repository.isCompanyEmptyOfUsers(id)) {
            throw new ParentDeletionException("This company cannot be deleted because it still has vendors.");
        }

        super.delete(id);
    }
}