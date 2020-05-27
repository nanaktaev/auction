package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.ParentDeletionException;
import by.company.auction.converters.CategoryConverter;
import by.company.auction.dto.CategoryDto;
import by.company.auction.model.Category;
import by.company.auction.repository.CategoryRepository;
import by.company.auction.validators.CategoryValidator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceTest extends AbstractTest {

    @SuppressWarnings("unused")
    @Spy
    private CategoryConverter categoryConverter;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryValidator categoryValidator;
    @InjectMocks
    private CategoryService categoryService;

    private static Category category;
    private static CategoryDto categoryDto;

    @BeforeClass
    public static void beforeAllTests() {

        category = new Category();
        category.setName("Immovables");

        categoryDto = new CategoryDto();
        categoryDto.setName("Immovables");
    }

    @Test
    public void findCategoryByName() {

        when(categoryRepository.findCategoryByName("Immovables")).thenReturn(category);

        CategoryDto receivedCategory = categoryService.findCategoryByName("Immovables");

        assertEquals(receivedCategory.getName(), "Immovables");
    }

    @Test
    public void create() {

        doNothing().when(categoryValidator).validate(categoryDto);
        when(categoryRepository.save(any())).thenReturn(category);

        CategoryDto createdCategory = categoryService.create(categoryDto);

        assertEquals(createdCategory.getName(), "Immovables");
    }

    @Test
    public void deleteEmptyCategory() {

        when(categoryRepository.isCategoryEmptyOfLots(1)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1);

        categoryService.delete(1);

        verify(categoryRepository, times(1)).deleteById(1);
    }

    @Test(expected = ParentDeletionException.class)
    public void deleteCategoryWithLots() {

        when(categoryRepository.isCategoryEmptyOfLots(1)).thenReturn(false);

        categoryService.delete(1);

        verify(categoryRepository, times(0)).deleteById(1);
    }
}