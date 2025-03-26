package portfolio.ecommerce.order.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SellerTest {

    @Test
    void sellerBuilderShouldCreateObjectCorrectly() {
        // Given
        Seller seller = Seller.builder()
                .sellerId(1L)
                .name("seller")
                .deleted(false)
                .build();

        // Then
        assertEquals(1L, seller.getSellerId());
        assertEquals("seller", seller.getName());
        assertFalse(seller.isDeleted());
    }

    @Test
    void jsonIgnoreAnnotationShouldExcludeDeletedField() throws JsonProcessingException {
        // Given
        Seller seller = Seller.builder()
                .sellerId(2L)
                .name("seller")
                .deleted(true)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(seller);

        // Then: JSON에 "deleted" 필드가 포함되지 않아야 함
        assertFalse(json.contains("deleted"));
    }

    @Test
    void setterShouldUpdateFieldsCorrectly() {
        // Given
        Seller seller = new Seller();
        seller.setSellerId(3L);
        seller.setName("seller");
        seller.setDeleted(true);

        // Then
        assertEquals(3L, seller.getSellerId());
        assertEquals("seller", seller.getName());
        assertTrue(seller.isDeleted());
    }
}
