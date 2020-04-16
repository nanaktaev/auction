package by.company.auction.services;

import by.company.auction.dao.CategoryDao;
import by.company.auction.model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryService extends AbstractService<Category, CategoryDao> {

    private static CategoryService categoryServiceInstance;
    private final Logger LOGGER = LogManager.getLogger(CategoryService.class);

    public Category findCategoryByName(String name) {

        LOGGER.debug("findCategoryByName() name = {}", name);
        return dao.findCategoryByName(name);

    }

    public static CategoryService getInstance() {
        if (categoryServiceInstance != null) {
            return categoryServiceInstance;
        }
        categoryServiceInstance = new CategoryService();
        categoryServiceInstance.setDao(CategoryDao.getInstance());
        return categoryServiceInstance;
    }
}
