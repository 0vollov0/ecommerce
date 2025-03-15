package portfolio.ecommerce.order.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import portfolio.ecommerce.order.dto.*;
import portfolio.ecommerce.order.entity.Category;
import portfolio.ecommerce.order.entity.Customer;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.entity.Seller;
import portfolio.ecommerce.order.repository.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StockLockRepository stockLockRepository;

    private Product product;
    private Seller seller;
    private Customer customer;
    private Category category;

    private final List<Long>  shouldDeleteIds = new ArrayList<>();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        customer = customerRepository.save(Customer.builder().name("1").amount(100000).build());
        category = categoryRepository.save(Category.builder().name("1").build());
        seller = sellerRepository.save(Seller.builder().name("1").build());
        product = productRepository.save(Product.builder().categoryId(category.getCategoryId()).sellerId(seller.getSellerId()).name("1").salesPrice(10).stock(1000).build());
    }

    @AfterEach
    void cleanUp() {
        shouldDeleteIds.forEach((id) -> {
            stockLockRepository.deleteByOrderId(id);
            orderRepository.deleteById(id);
        });
        shouldDeleteIds.clear();

        productRepository.deleteById(product.getProductId());
        customerRepository.deleteById(customer.getCustomerId());
        categoryRepository.deleteById(category.getCategoryId());
        sellerRepository.deleteById(seller.getSellerId());
    }

    @Test
    void order() throws Exception {
        OrderDto dto = new OrderDto(product.getProductId(), customer.getCustomerId(), 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.orderId").isNumber())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseJson);

        Long orderId = jsonNode.get("orderId").asLong();

        shouldDeleteIds.add(orderId);
    }
}
