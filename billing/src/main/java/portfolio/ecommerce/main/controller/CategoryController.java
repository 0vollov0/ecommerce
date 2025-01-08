package portfolio.ecommerce.main.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.ecommerce.main.dto.CreateCategoryDTO;
import portfolio.ecommerce.main.entity.Category;
import portfolio.ecommerce.main.service.CategoryService;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Category create(@Valid CreateCategoryDTO dto) {
        return categoryService.create(dto);
    }

    @GetMapping
    public Iterable<Category> get() {
        return categoryService.find();
    }
}
