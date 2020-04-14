package by.company.auction.services;

import by.company.auction.dao.CompanyDao;
import by.company.auction.model.Company;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class CompanyServiceTest extends AbstractService {

    private Company company;
    private CompanyService companyService;
    private CompanyDao companyDao;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(CompanyDao.class);
        PowerMockito.when(CompanyDao.getInstance()).thenReturn(mock(CompanyDao.class));
        MockitoAnnotations.initMocks(this);

        companyDao = CompanyDao.getInstance();
        companyService = CompanyService.getInstance();

        company = new Company();
        company.setName("Netcracker");

    }

    @Test
    @PrepareForTest({CompanyService.class, CompanyDao.class})
    public void findCompanyByName() {

        when(companyDao.findCompanyByName(anyString())).thenReturn(company);

        Company receivedCompany = companyService.findCompanyByName(anyString());

        assertNotNull(receivedCompany);

    }

}