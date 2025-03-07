package portfolio.ecommerce.order.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.order.dto.CreateProductDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateProductDto;
import portfolio.ecommerce.order.entity.Category;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.entity.Seller;
import portfolio.ecommerce.order.repository.CategoryRepository;
import portfolio.ecommerce.order.repository.ProductRepository;
import portfolio.ecommerce.order.repository.SellerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    public Product create(CreateProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);
        Seller seller = sellerRepository.findById(dto.getSellerId()).orElseThrow(EntityNotFoundException::new);
        return productRepository.save(
                Product.builder()
                .name(dto.getName())
                .category(category)
                .seller(seller)
                .stock(dto.getStock())
                .salesPrice(dto.getSalesPrice())
                .build()
        );
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> find(RequestPagingDto dto) {
        return productRepository.findAllByDeleted(false, PageRequest.of(dto.getPage(), dto.getPageSize(), Sort.by("createdAt").descending()));
    }

    @Transactional
    public Product update(Long id, UpdateProductDto dto) {
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(EntityNotFoundException::new);
        product.setName(dto.getName());
        product.setStock(dto.getStock());
        product.setSalesPrice(dto.getSalesPrice());
        product.setCategory(category);
        return product;
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        product.setDeleted(true);
    }
}
