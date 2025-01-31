package portfolio.ecommerce.main.controller;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.ecommerce.main.dto.CreateProductDTO;
import portfolio.ecommerce.main.entity.Product;
import portfolio.ecommerce.main.service.ProductService;

@RequestMapping("/products")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public Product create(@Valid CreateProductDTO dto) throws BadRequestException {
        return productService.create(dto);
    }

    @GetMapping
    public Iterable<Product> get() {
        return productService.find();
    }
}
