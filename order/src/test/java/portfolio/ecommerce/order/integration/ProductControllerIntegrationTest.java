package portfolio.ecommerce.order.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import portfolio.ecommerce.order.dto.CreateProductDto;
import portfolio.ecommerce.order.dto.CreateProductDto;
import portfolio.ecommerce.order.dto.UpdateProductDto;
import portfolio.ecommerce.order.entity.Category;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.entity.Seller;
import portfolio.ecommerce.order.repository.CategoryRepository;
import portfolio.ecommerce.order.repository.ProductRepository;
import portfolio.ecommerce.order.repository.ProductRepository;
import portfolio.ecommerce.order.repository.SellerRepository;
import portfolio.ecommerce.order.service.ProductService;
import portfolio.ecommerce.order.service.ProductService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    private Seller seller;
    private Category category;

    private final List<Long>  shouldDeleteIds = new ArrayList<>();

    @BeforeEach()
    void setUp() {
        category = categoryRepository.save(Category.builder().name("TEST_CATEGORY").build());
        seller = sellerRepository.save(Seller.builder().name("TEST_SELLER").build());
    }

    @AfterEach
    @Transactional
    void cleanUp() {
        shouldDeleteIds.forEach((id) -> productRepository.deleteById(id));
        productRepository.flush();
        shouldDeleteIds.clear();

        categoryRepository.deleteById(category.getCategoryId());
        sellerRepository.deleteById(seller.getSellerId());
    }

    @Test
    void createProduct() throws Exception {
        CreateProductDto dto = new CreateProductDto(category.getCategoryId(), seller.getSellerId(), "12", 100, 5000);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(category.getCategoryId()))
                .andExpect(jsonPath("$.sellerId").value(seller.getSellerId()))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseJson);

        Long productId = jsonNode.get("productId").asLong();

        shouldDeleteIds.add(productId);
    }

    @Test
    void updateProduct() throws Exception {
        Product savedProduct = productService.create(
                CreateProductDto.builder().name("TEST_PRODUCT")
                .categoryId(category.getCategoryId())
                .sellerId(seller.getSellerId())
                .stock(5000)
                .salesPrice(1000).build()
        );
        UpdateProductDto dto = UpdateProductDto.builder().categoryId(category.getCategoryId()).name("34").salesPrice(200).stock(3000).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(patch("/products/{id}", savedProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("34"))
                .andExpect(jsonPath("$.salesPrice").value(200))
                .andExpect(jsonPath("$.stock").value(3000))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseJson);

        Long productId = jsonNode.get("productId").asLong();

        shouldDeleteIds.add(productId);
    }

    @Test
    void deleteProduct() throws Exception {
        Product savedProduct = productService.create(
                CreateProductDto.builder().name("TEST_PRODUCT")
                        .categoryId(category.getCategoryId())
                        .sellerId(seller.getSellerId())
                        .stock(5000)
                        .salesPrice(1000).build()
        );

        mockMvc.perform(delete("/products/{id}", savedProduct.getProductId()))
                .andExpect(status().isOk());
        assertTrue(productRepository.existsByProductIdAndDeletedTrue(savedProduct.getProductId()));
        productRepository.deleteById(savedProduct.getProductId());
    }

    @Test
    void getProducts() throws Exception {
        mockMvc.perform(get("/products")
                        .param("page", String.valueOf(0))
                        .param("pageSize", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content").isArray());
    }
}
