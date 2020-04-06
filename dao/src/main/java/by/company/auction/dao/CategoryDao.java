package by.company.auction.dao;

import by.company.auction.model.Category;

public class CategoryDao extends AbstractDao<Category> {

    private static CategoryDao categoryDaoInstance;

    private CategoryDao() {
        super(Category.class);
    }

    public Category findByName(String name) {
        for (Category category : findAll()) {
            if (name.equals(category.getName())) {
                return category;
            }
        }
        return null;
    }

    public static CategoryDao getInstance() {
        if (categoryDaoInstance != null) {
            return categoryDaoInstance;
        }
        categoryDaoInstance = new CategoryDao();
        return categoryDaoInstance;
    }

}
