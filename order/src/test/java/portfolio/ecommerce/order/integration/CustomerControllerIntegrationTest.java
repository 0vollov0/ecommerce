package portfolio.ecommerce.order.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import portfolio.ecommerce.order.dto.CreateCustomerDto;
import portfolio.ecommerce.order.dto.UpdateCustomerDto;
import portfolio.ecommerce.order.entity.Customer;
import portfolio.ecommerce.order.repository.CustomerRepository;
import portfolio.ecommerce.order.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;

    private final List<Long>  shouldDeleteIds = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        shouldDeleteIds.forEach((id) -> customerRepository.deleteById(id));
        shouldDeleteIds.clear();
    }

    @BeforeEach()
    void setUp() {
    }

    @Test
    void createCustomer() throws Exception {
        CreateCustomerDto dto = new CreateCustomerDto("TESTER", 1000);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").isNumber())
                .andExpect(jsonPath("$.name").value("TESTER"))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseJson);

        Long customerId = jsonNode.get("customerId").asLong();

        shouldDeleteIds.add(customerId);
    }

    @Test
    void updateCustomer() throws Exception {
        Customer customer = customerRepository.save(Customer.builder().name("TESTER").amount(10).build());
        UpdateCustomerDto dto = new UpdateCustomerDto("UPDATER", 1000);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(patch("/customers/{id}", customer.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customer.getCustomerId()))
                .andExpect(jsonPath("$.name").value("UPDATER"))
                .andExpect(jsonPath("$.amount").value(1000))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseJson);

        Long customerId = jsonNode.get("customerId").asLong();

        shouldDeleteIds.add(customerId);
    }

    @Test
    void deleteCustomer() throws Exception {
        Customer customer = customerRepository.save(Customer.builder().name("TESTER").amount(10).build());

        mockMvc.perform(delete("/customers/{id}", customer.getCustomerId()))
                .andExpect(status().isOk());
        assertTrue(customerRepository.existsByCustomerIdAndDeletedTrue(customer.getCustomerId()));
        customerRepository.deleteById(customer.getCustomerId());
    }

    @Test
    void getCustomers() throws Exception {
        mockMvc.perform(get("/customers")
                        .param("page", String.valueOf(0))
                        .param("pageSize", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content").isArray());
    }
}
