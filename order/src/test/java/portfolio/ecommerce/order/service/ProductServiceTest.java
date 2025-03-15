package portfolio.ecommerce.order.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import portfolio.ecommerce.order.dto.CreateProductDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateProductDto;
import portfolio.ecommerce.order.entity.Category;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.entity.Seller;
import portfolio.ecommerce.order.repository.CategoryRepository;
import portfolio.ecommerce.order.repository.ProductRepository;
import portfolio.ecommerce.order.repository.SellerRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private EntityManager entityManager;

    private ProductService productService;

    private Long productId = 1L;
    private Long customerId = 2L;
    private Long sellerId = 3L;
    private Long categoryId = 4L;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, categoryRepository, entityManager);
    }

    @Test
    void createProduct() {
        CreateProductDto dto = new CreateProductDto(categoryId, sellerId, "TEST_PRODUCT", 100, 5000);
        Category categoryRef = entityManager.getReference(Category.class, dto.getCategoryId());
        Seller sellerRef = entityManager.getReference(Seller.class, dto.getSellerId());

        Product product = Product.builder()
                .name(dto.getName())
                .categoryId(productId)
                .sellerId(sellerId)
                .stock(dto.getStock())
                .salesPrice(dto.getSalesPrice())
                .build();

        product.setCategory(categoryRef);
        product.setSeller(sellerRef);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.create(dto);

        assertNotNull(savedProduct);
        assertEquals(dto.getName(), savedProduct.getName());
        assertEquals(dto.getStock(), savedProduct.getStock());
        assertEquals(dto.getSalesPrice(), savedProduct.getSalesPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void findProductById() {
        Product product = Product.builder().productId(1L).name("TEST_PRODUCT").stock(100).salesPrice(5000).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.findById(1L);

        assertTrue(foundProduct.isPresent());
        assertEquals("TEST_PRODUCT", foundProduct.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void findProducts() {
        RequestPagingDto dto = new RequestPagingDto(0, 10);
        Page<Product> page = new PageImpl<>(Collections.singletonList(new Product()));

        when(productRepository.findAllByDeleted(eq(false), any(PageRequest.class))).thenReturn(page);

        Page<Product> result = productService.find(dto);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(productRepository, times(1)).findAllByDeleted(eq(false), any(PageRequest.class));
    }

    @Test
    void updateProduct() {
        UpdateProductDto dto = new UpdateProductDto(1L, "TEST_PRODUCT_UPDATED", 6000, 150);
        Product product = Product.builder().productId(1L).name("TEST_PRODUCT").stock(100).salesPrice(5000).build();
        Category category = Category.builder().categoryId(1L).name("TEST_CATEGORY").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Product updatedProduct = productService.update(1L, dto);

        assertNotNull(updatedProduct);
        assertEquals("TEST_PRODUCT_UPDATED", updatedProduct.getName());
        assertEquals(150, updatedProduct.getStock());
        assertEquals(6000, updatedProduct.getSalesPrice());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void deleteProduct() {
        Product product = Product.builder().productId(1L).name("TEST_PRODUCT").stock(100).salesPrice(5000).deleted(false).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        assertTrue(product.isDeleted());
        verify(productRepository, times(1)).findById(1L);
    }
}