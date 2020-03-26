package by.company.auction.dao;

import by.company.auction.model.Category;

public class CategoryDao extends AbstractDao<Category> {

    private static CategoryDao categoryDaoInstance;

    private CategoryDao() {
    }

    public static CategoryDao getInstance() {
        if (categoryDaoInstance != null) {
            return categoryDaoInstance;
        }
        categoryDaoInstance = new CategoryDao();
        return categoryDaoInstance;
    }

}
