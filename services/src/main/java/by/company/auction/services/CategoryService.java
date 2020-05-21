package by.company.auction.services;

import by.company.auction.model.Category;
import by.company.auction.repository.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
public class CategoryService extends AbstractService<Category, CategoryRepository> {

    protected CategoryService(CategoryRepository repository) {
        super(repository);
    }

    public Category findCategoryByName(String name) {

        log.debug("findCategoryByName() name = {}", name);
        return repository.findCategoryByName(name);

    }

}
