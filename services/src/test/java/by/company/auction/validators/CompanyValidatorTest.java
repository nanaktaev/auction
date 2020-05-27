package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.EntityAlreadyExistsException;
import by.company.auction.dto.CompanyDto;
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

    private static CompanyDto companyDto;

    @BeforeClass
    public static void beforeAllTests() {

        companyDto = new CompanyDto();
        companyDto.setName("Netcracker");
    }

    @Test
    public void validateSuccess() {

        when(companyService.findCompanyByName("Netcracker")).thenReturn(null);

        companyValidator.validate(companyDto);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void validateAlreadyExists() {

        when(companyService.findCompanyByName("Netcracker")).thenReturn(companyDto);

        companyValidator.validate(companyDto);
    }
}