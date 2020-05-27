package by.company.auction.validators;

import by.company.auction.common.exceptions.EntityAlreadyExistsException;
import by.company.auction.dto.CategoryDto;
import by.company.auction.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategoryValidator {

    @Autowired
    private CategoryService categoryService;

    public void validate(CategoryDto categoryDto) {

        log.debug("validate() categoryDto = {}", categoryDto);

        if (categoryService.findCategoryByName(categoryDto.getName()) != null) {
            throw new EntityAlreadyExistsException("This category has already been added.");
        }
    }

}
