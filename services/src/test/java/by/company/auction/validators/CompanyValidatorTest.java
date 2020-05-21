package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.model.Company;
import by.company.auction.services.CompanyService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class CompanyValidatorTest extends AbstractTest {

    @Mock
    private CompanyService companyService;
    @InjectMocks
    private CompanyValidator companyValidator;

    private static Company company;

    @BeforeClass
    public static void beforeAllTests() {

        company = new Company();
        company.setName("Minsk");

    }

    @Test
    public void validateSuccess() {

        when(companyService.findCompanyByName("Minsk")).thenReturn(null);

        companyValidator.validate(company);

    }

    @Test(expected = AlreadyExistsException.class)
    public void validateAlreadyExists() {

        when(companyService.findCompanyByName("Minsk")).thenReturn(company);

        companyValidator.validate(company);

    }

}