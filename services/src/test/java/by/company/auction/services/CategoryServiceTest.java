package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.model.Category;
import by.company.auction.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CategoryServiceTest extends AbstractTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @Before
    public void beforeEachTest() {

        category = new Category();
        category.setName("Immovables");

    }

    @Test
    public void findCategoryByName() {

        when(categoryRepository.findCategoryByName("Immovables")).thenReturn(category);

        Category receivedCategory = categoryService.findCategoryByName("Immovables");

        assertNotNull(receivedCategory);

    }

}