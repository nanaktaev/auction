package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.model.Category;
import by.company.auction.services.CategoryService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class CategoryValidatorTest extends AbstractTest {

    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private CategoryValidator categoryValidator;

    private static Category category;

    @BeforeClass
    public static void beforeAllTests() {

        category = new Category();
        category.setName("Minsk");

    }

    @Test
    public void validateSuccess() {

        when(categoryService.findCategoryByName("Minsk")).thenReturn(null);

        categoryValidator.validate(category);

    }

    @Test(expected = AlreadyExistsException.class)
    public void validateAlreadyExists() {

        when(categoryService.findCategoryByName("Minsk")).thenReturn(category);

        categoryValidator.validate(category);

    }

}