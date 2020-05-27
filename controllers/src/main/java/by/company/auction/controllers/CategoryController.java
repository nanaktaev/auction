package by.company.auction.controllers;

import by.company.auction.dto.CategoryDto;
import by.company.auction.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories() {

        log.debug("getAllCategories()");

        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Integer id) {

        log.debug("getCategoryById() id = {}", id);

        return categoryService.findById(id);
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {

        log.debug("createCategory() categoryDto = {}", categoryDto);

        return categoryService.create(categoryDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {

        log.debug("deleteCategory() id = {}", id);

        categoryService.delete(id);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Integer id, @RequestBody @Valid CategoryDto categoryDto) {

        categoryDto.setId(id);

        log.debug("updateCategory() categoryDto = {}", categoryDto);

        return categoryService.update(categoryDto);
    }

}
