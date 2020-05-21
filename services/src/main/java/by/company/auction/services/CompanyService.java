package by.company.auction.services;

import by.company.auction.model.Company;
import by.company.auction.repository.CompanyRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
public class CompanyService extends AbstractService<Company, CompanyRepository> {

    protected CompanyService(CompanyRepository repository) {
        super(repository);
    }

    public Company findCompanyByName(String name) {

        log.debug("findCompanyByName() name = {}", name);
        return repository.findCompanyByName(name);

    }

}
