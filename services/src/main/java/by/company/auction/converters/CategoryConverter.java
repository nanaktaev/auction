package by.company.auction.converters;

import by.company.auction.dto.CategoryDto;
import by.company.auction.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter extends AbstractConverter<Category, CategoryDto> {

    @Override
    public CategoryDto convertToDto(Category category) {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        return categoryDto;
    }

    @Override
    public Category convertToEntity(CategoryDto categoryDto) {

        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());

        return category;
    }
}