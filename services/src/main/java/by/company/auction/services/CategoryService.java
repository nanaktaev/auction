package by.company.auction.services;

import by.company.auction.common.exceptions.ParentDeletionException;
import by.company.auction.converters.CategoryConverter;
import by.company.auction.dto.CategoryDto;
import by.company.auction.model.Category;
import by.company.auction.repository.CategoryRepository;
import by.company.auction.validators.CategoryValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CategoryService extends AbstractService<Category, CategoryDto, CategoryRepository, CategoryConverter> {

    @Autowired
    private CategoryValidator categoryValidator;

    protected CategoryService(CategoryRepository repository, CategoryConverter converter) {
        super(repository, converter);
    }

    public CategoryDto findCategoryByName(String name) {

        log.debug("findCategoryByName() name = {}", name);

        Category category = repository.findCategoryByName(name);

        if (category == null) {
            return null;
        }

        return converter.convertToDto(category);
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        log.debug("create() categoryDto = {}", categoryDto);
        categoryValidator.validate(categoryDto);

        return super.create(categoryDto);
    }

    @Override
    public void delete(Integer id) {

        log.debug("delete() categoryId = {}", id);

        if (!repository.isCategoryEmptyOfLots(id)) {
            throw new ParentDeletionException("This category cannot be deleted because it still has lots.");
        }

        super.delete(id);
    }
}
