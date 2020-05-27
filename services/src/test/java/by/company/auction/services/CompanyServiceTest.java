package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.ParentDeletionException;
import by.company.auction.converters.CompanyConverter;
import by.company.auction.dto.CompanyDto;
import by.company.auction.model.Company;
import by.company.auction.repository.CompanyRepository;
import by.company.auction.validators.CompanyValidator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CompanyServiceTest extends AbstractTest {

    @SuppressWarnings("unused")
    @Spy
    private CompanyConverter companyConverter;
    @Mock
    private CompanyValidator companyValidator;
    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private CompanyService companyService;

    private static Company company;
    private static CompanyDto companyDto;

    @BeforeClass
    public static void beforeAllTests() {

        company = new Company();
        company.setName("Netcracker");

        companyDto = new CompanyDto();
        companyDto.setName("Netcracker");
    }

    @Test
    public void findCompanyByName() {

        when(companyRepository.findCompanyByName("Netcracker")).thenReturn(company);

        CompanyDto receivedCompany = companyService.findCompanyByName("Netcracker");

        assertEquals(receivedCompany.getName(), "Netcracker");
    }

    @Test
    public void create() {

        doNothing().when(companyValidator).validate(companyDto);
        when(companyRepository.save(any())).thenReturn(company);

        CompanyDto createdCategory = companyService.create(companyDto);

        assertEquals(createdCategory.getName(), "Netcracker");
    }

    @Test
    public void deleteEmptyCompany() {

        when(companyRepository.isCompanyEmptyOfLots(1)).thenReturn(true);
        when(companyRepository.isCompanyEmptyOfUsers(1)).thenReturn(true);
        doNothing().when(companyRepository).deleteById(1);

        companyService.delete(1);

        verify(companyRepository, times(1)).deleteById(1);
    }

    @Test(expected = ParentDeletionException.class)
    public void deleteCompanyWithLots() {

        when(companyRepository.isCompanyEmptyOfLots(1)).thenReturn(false);

        companyService.delete(1);

        verify(companyRepository, times(0)).deleteById(1);
    }

    @Test(expected = ParentDeletionException.class)
    public void deleteCompanyWithUsers() {

        when(companyRepository.isCompanyEmptyOfLots(1)).thenReturn(true);
        when(companyRepository.isCompanyEmptyOfUsers(1)).thenReturn(false);

        companyService.delete(1);

        verify(companyRepository, times(0)).deleteById(1);
    }
}