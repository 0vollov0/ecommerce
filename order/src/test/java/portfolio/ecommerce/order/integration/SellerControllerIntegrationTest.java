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
import portfolio.ecommerce.order.dto.CreateSellerDto;
import portfolio.ecommerce.order.dto.UpdateSellerDto;
import portfolio.ecommerce.order.entity.Seller;
import portfolio.ecommerce.order.repository.SellerRepository;
import portfolio.ecommerce.order.service.SellerService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SellerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private SellerService sellerService;

    private final List<Long>  shouldDeleteIds = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        shouldDeleteIds.forEach((id) -> sellerRepository.deleteById(id));
        shouldDeleteIds.clear();
    }

    @BeforeEach()
    void setUp() {
    }

    @Test
    void createSeller() throws Exception {
        CreateSellerDto dto = new CreateSellerDto("TestSeller");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sellerId").isNumber())
                .andExpect(jsonPath("$.name").value("TestSeller"))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseJson);

        Long sellerId = jsonNode.get("sellerId").asLong();

        shouldDeleteIds.add(sellerId);
    }

    @Test
    void updateSeller() throws Exception {
        Seller seller = sellerRepository.save(Seller.builder().name("TestSeller").build());
        UpdateSellerDto dto = new UpdateSellerDto("UpdateSeller");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(patch("/sellers/{id}", seller.getSellerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerId").value(seller.getSellerId()))
                .andExpect(jsonPath("$.name").value("UpdateSeller"))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseJson);

        Long sellerId = jsonNode.get("sellerId").asLong();

        shouldDeleteIds.add(sellerId);
    }

    @Test
    void deleteSeller() throws Exception {
        Seller seller = sellerRepository.save(Seller.builder().name("TestSeller").build());
        mockMvc.perform(delete("/sellers/{id}", seller.getSellerId()))
                .andExpect(status().isOk());
        assertTrue(sellerRepository.existsBySellerIdAndDeletedTrue(seller.getSellerId()));
        sellerRepository.deleteById(seller.getSellerId());
    }

    @Test
    void getSellers() throws Exception {
        mockMvc.perform(get("/sellers")
                        .param("page", String.valueOf(0))
                        .param("pageSize", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content").isArray());
    }
}
