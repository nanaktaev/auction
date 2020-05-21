package by.company.auction.validators;

import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.model.Category;
import by.company.auction.services.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CategoryValidator {

    @Autowired
    private CategoryService categoryService;

    public void validate(Category category) {

        log.debug("validate() category = {}", category);

        if (categoryService.findCategoryByName(category.getName()) != null) {
            throw new AlreadyExistsException("Категория с таким именем уже существует.");
        }
    }

}
