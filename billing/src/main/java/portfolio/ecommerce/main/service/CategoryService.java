package portfolio.ecommerce.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.main.dto.CreateCategoryDTO;
import portfolio.ecommerce.main.entity.Category;
import portfolio.ecommerce.main.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category create(CreateCategoryDTO dto) {
        Category category = toEntity(dto);
        return categoryRepository.save(category);
    }

    public Iterable<Category> find() {
        return categoryRepository.findAll();
    }

    private Category toEntity(CreateCategoryDTO dto) {
        return Category.builder().name(dto.getName()).build();
    }
}
