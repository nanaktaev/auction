package by.company.auction.services;

import by.company.auction.dao.CategoryDao;
import by.company.auction.model.Category;

public class CategoryService extends AbstractService<Category, CategoryDao> {

    private static CategoryService categoryServiceInstance;

    private CategoryService() {
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
