package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NotFoundException;
import by.company.auction.model.Category;
import by.company.auction.model.Lot;
import by.company.auction.model.Town;
import by.company.auction.services.CategoryService;
import by.company.auction.services.TownService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LotValidatorTest extends AbstractTest {

    private CategoryService categoryService;
    private TownService townService;
    private LotValidator lotValidator;
    private Lot lot;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(CategoryService.class);
        PowerMockito.when(CategoryService.getInstance()).thenReturn(mock(CategoryService.class));
        PowerMockito.mockStatic(TownService.class);
        PowerMockito.when(TownService.getInstance()).thenReturn(mock(TownService.class));
        MockitoAnnotations.initMocks(this);

        categoryService = CategoryService.getInstance();
        townService = TownService.getInstance();
        lotValidator = LotValidator.getInstance();

        lot = new Lot();
        lot.setCompanyId(1);
        lot.setCategoryId(1);
        lot.setTownId(1);

    }

    @Test
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateClosingDateSuccess() {

        lot.setCloses(LocalDateTime.now().plusHours(1));
        lotValidator.validateClosingDate(lot);

    }

    @Test(expected = BusinessException.class)
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateClosingDateFailure() {

        lot.setCloses(LocalDateTime.now());
        lotValidator.validateClosingDate(lot);

    }

    @Test
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateStepSuccess() {

        lot.setStep(new BigDecimal(10));
        lotValidator.validateStep(lot);

    }

    @Test(expected = BusinessException.class)
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateStepFailure() {

        lot.setStep(new BigDecimal(0));
        lotValidator.validateStep(lot);

    }

    @Test
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateStartPriceSuccess() {

        lot.setPriceStart(new BigDecimal(100));
        lotValidator.validateStartPrice(lot);

    }

    @Test(expected = BusinessException.class)
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateStartPriceFailure() {

        lot.setPriceStart(new BigDecimal(0));
        lotValidator.validateStartPrice(lot);

    }

    @Test
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateOwnershipSuccess() {

        lotValidator.validateOwnership(lot, 1);

    }

    @Test(expected = BusinessException.class)
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateOwnershipFailure() {

        lotValidator.validateOwnership(lot, 2);

    }

    @Test
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateCategorySuccess() {

        Category category = new Category();

        when(categoryService.findById(anyInt())).thenReturn(category);

        lotValidator.validateCategory(lot);

    }

    @Test(expected = NotFoundException.class)
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateCategoryFailure() {

        when(categoryService.findById(anyInt())).thenReturn(null);

        lotValidator.validateCategory(lot);

    }

    @Test
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateTownSuccess() {

        Town town = new Town();

        when(townService.findById(anyInt())).thenReturn(town);

        lotValidator.validateTown(lot);

    }

    @Test(expected = NotFoundException.class)
    @PrepareForTest({CategoryService.class, TownService.class, LotValidator.class})
    public void validateTownFailure() {

        when(townService.findById(anyInt())).thenReturn(null);

        lotValidator.validateTown(lot);

    }

}