package portfolio.ecommerce.main.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.main.dto.CreateProductDTO;
import portfolio.ecommerce.main.entity.Category;
import portfolio.ecommerce.main.entity.Product;
import portfolio.ecommerce.main.repository.CategoryRepository;
import portfolio.ecommerce.main.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Product create(CreateProductDTO dto) throws BadRequestException {
        Product product = toEntity(dto);
        return productRepository.save(product);
    }

    public Iterable<Product> find() {
        return productRepository.findAll();
    }

    private Product toEntity(CreateProductDTO dto) throws BadRequestException {
        Category category = categoryRepository.findById(dto.getCategory_id()).orElseThrow(() -> new BadRequestException(""));
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .category(category)
                .build();
    }
}
