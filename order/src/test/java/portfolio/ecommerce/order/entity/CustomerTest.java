package portfolio.ecommerce.order.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void customerBuilderShouldCreateObjectCorrectly() {
        // Given
        Customer customer = Customer.builder()
                .customerId(1L)
                .name("customer")
                .amount(1000)
                .deleted(false)
                .build();

        // Then
        assertEquals(1L, customer.getCustomerId());
        assertEquals("customer", customer.getName());
        assertEquals(1000, customer.getAmount());
        assertFalse(customer.isDeleted());
    }

    @Test
    void jsonIgnoreAnnotationShouldExcludeDeletedField() throws JsonProcessingException {
        // Given
        Customer customer = Customer.builder()
                .customerId(2L)
                .name("customer")
                .amount(500)
                .deleted(true)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(customer);

        // Then: JSON에 "deleted" 필드가 포함되지 않아야 함
        assertFalse(json.contains("deleted"));
    }

    @Test
    void setterShouldUpdateFieldsCorrectly() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(3L);
        customer.setName("customer");
        customer.setAmount(750);
        customer.setDeleted(true);

        // Then
        assertEquals(3L, customer.getCustomerId());
        assertEquals("customer", customer.getName());
        assertEquals(750, customer.getAmount());
        assertTrue(customer.isDeleted());
    }
}
