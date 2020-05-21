package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.model.Category;
import by.company.auction.model.Company;
import by.company.auction.model.Lot;
import by.company.auction.model.Town;
import by.company.auction.services.CategoryService;
import by.company.auction.services.TownService;
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

    private static Lot lot;

    @BeforeClass
    public static void beforeAllTests() {

        Company company = new Company();
        Category category = new Category();
        Town town = new Town();

        company.setId(1);
        category.setId(1);
        town.setId(1);

        lot = new Lot();
        lot.setCompany(company);
        lot.setCategory(category);
        lot.setTown(town);

    }

    @Test
    public void validateClosingDateSuccess() {

        lot.setCloses(LocalDateTime.now().plusHours(1));
        lotValidator.validateClosingDate(lot);

    }

    @Test(expected = BusinessException.class)
    public void validateClosingDateFailure() {

        lot.setCloses(LocalDateTime.now());
        lotValidator.validateClosingDate(lot);

    }

    @Test
    public void validateStepSuccess() {

        lot.setStep(new BigDecimal(10));
        lotValidator.validateStep(lot);

    }

    @Test(expected = BusinessException.class)
    public void validateStepFailure() {

        lot.setStep(new BigDecimal(0));
        lotValidator.validateStep(lot);

    }

    @Test
    public void validateStartPriceSuccess() {

        lot.setPriceStart(new BigDecimal(100));
        lotValidator.validateStartPrice(lot);

    }

    @Test(expected = BusinessException.class)
    public void validateStartPriceFailure() {

        lot.setPriceStart(new BigDecimal(0));
        lotValidator.validateStartPrice(lot);

    }

    @Test
    public void validateOwnershipSuccess() {

        lotValidator.validateOwnership(lot, 1);

    }

    @Test(expected = BusinessException.class)
    public void validateOwnershipFailure() {

        lotValidator.validateOwnership(lot, 2);

    }

    @Test
    public void validateCategorySuccess() {

        when(categoryService.exists(1)).thenReturn(true);

        lotValidator.validateCategory(lot);

    }

    @Test(expected = NoSuchEntityException.class)
    public void validateCategoryFailure() {

        when(categoryService.exists(1)).thenReturn(false);

        lotValidator.validateCategory(lot);

    }

    @Test
    public void validateTownSuccess() {

        when(townService.exists(1)).thenReturn(true);

        lotValidator.validateTown(lot);

    }

    @Test(expected = NoSuchEntityException.class)
    public void validateTownFailure() {

        when(townService.exists(1)).thenReturn(false);

        lotValidator.validateTown(lot);

    }

}