package by.company.auction.services;

import by.company.auction.dao.CategoryDao;
import by.company.auction.model.Category;
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
public class CategoryServiceTest extends AbstractService {

    private Category category;
    private CategoryService categoryService;
    private CategoryDao categoryDao;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(CategoryDao.class);
        PowerMockito.when(CategoryDao.getInstance()).thenReturn(mock(CategoryDao.class));
        MockitoAnnotations.initMocks(this);

        categoryDao = CategoryDao.getInstance();
        categoryService = CategoryService.getInstance();

        category = new Category();
        category.setName("Netcracker");

    }

    @Test
    @PrepareForTest({CategoryService.class, CategoryDao.class})
    public void findCategoryByName() {

        when(categoryDao.findCategoryByName(anyString())).thenReturn(category);

        Category receivedCategory = categoryService.findCategoryByName(anyString());

        assertNotNull(receivedCategory);

    }

}