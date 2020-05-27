package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.EntityAlreadyExistsException;
import by.company.auction.dto.CategoryDto;
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

    private static CategoryDto categoryDto;

    @BeforeClass
    public static void beforeAllTests() {

        categoryDto = new CategoryDto();
        categoryDto.setName("Immovables");
    }

    @Test
    public void validateSuccess() {

        when(categoryService.findCategoryByName("Immovables")).thenReturn(null);

        categoryValidator.validate(categoryDto);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void validateAlreadyExists() {

        when(categoryService.findCategoryByName("Immovables")).thenReturn(categoryDto);

        categoryValidator.validate(categoryDto);
    }
}