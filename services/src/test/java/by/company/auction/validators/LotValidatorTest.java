package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.dto.CategoryDto;
import by.company.auction.dto.CompanyDto;
import by.company.auction.dto.LotDto;
import by.company.auction.dto.TownDto;
import by.company.auction.services.CategoryService;
import by.company.auction.services.TownService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

public class LotValidatorTest extends AbstractTest {

    @Mock
    private CategoryService categoryService;
    @Mock
    private TownService townService;
    @InjectMocks
    private LotValidator lotValidator;

    private static LotDto lotDto;

    @BeforeClass
    public static void beforeAllTests() {

        CompanyDto companyDto = new CompanyDto();
        CategoryDto categoryDto = new CategoryDto();
        TownDto townDto = new TownDto();

        companyDto.setId(1);
        categoryDto.setId(1);
        townDto.setId(1);

        lotDto = new LotDto();
        lotDto.setCompany(companyDto);
        lotDto.setCategory(categoryDto);
        lotDto.setTown(townDto);
        lotDto.setTitle("Lot");
    }

    @Before
    public void beforeEachTest() {

        lotDto.setCloses(LocalDateTime.now().plusDays(1));
        lotDto.setDescription("Description.");
        lotDto.setStep(new BigDecimal(10));
        lotDto.setPriceStart(new BigDecimal(100));
    }

    @Test
    public void validateUpdateSuccess() {

        when(categoryService.exists(1)).thenReturn(true);
        when(townService.exists(1)).thenReturn(true);

        lotValidator.validateUpdate(lotDto);
    }

    @Test(expected = BusinessException.class)
    public void validateUpdateClosingDateTooSoon() {

        lotDto.setCloses(LocalDateTime.now().plusMinutes(1));

        lotValidator.validateUpdate(lotDto);
    }

    @Test(expected = NoSuchEntityException.class)
    public void validateUpdateCategoryDoesNotExist() {

        when(categoryService.exists(1)).thenReturn(false);

        lotValidator.validateUpdate(lotDto);
    }

    @Test(expected = NoSuchEntityException.class)
    public void validateUpdateTownDoesNotExist() {

        when(categoryService.exists(1)).thenReturn(true);
        when(townService.exists(1)).thenReturn(false);

        lotValidator.validateUpdate(lotDto);
    }

    @Test
    public void validateCreationSuccess() {

        when(categoryService.exists(1)).thenReturn(true);
        when(townService.exists(1)).thenReturn(true);

        lotValidator.validate(lotDto);
    }

    @Test(expected = NullPointerException.class)
    public void validateCreationPropertyNotSet() {

        lotDto.setDescription(null);

        lotValidator.validate(lotDto);
    }

    @Test(expected = BusinessException.class)
    public void validateCreationStepTooSmall() {

        lotDto.setStep(new BigDecimal(0));

        lotValidator.validate(lotDto);
    }

    @Test(expected = BusinessException.class)
    public void validateCreationPriceTooSmall() {

        lotDto.setPriceStart(new BigDecimal(0));

        lotValidator.validate(lotDto);
    }
}