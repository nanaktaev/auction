package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.model.Company;
import by.company.auction.repository.CompanyRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CompanyServiceTest extends AbstractTest {

    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private CompanyService companyService;

    private Company company;

    @Before
    public void beforeEachTest() {

        company = new Company();
        company.setName("Netcracker");

    }

    @Test
    public void findCompanyByName() {

        when(companyRepository.findCompanyByName("Netcracker")).thenReturn(company);

        Company receivedCompany = companyService.findCompanyByName("Netcracker");

        assertNotNull(receivedCompany);

    }

}