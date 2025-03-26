package portfolio.ecommerce.worker.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    @Test
    void orderBuilderShouldCreateObjectCorrectly() {
        Order order = Order.builder()
                .orderId(100L)
                .customerId(1L)
                .sellerId(2L)
                .productId(3L)
                .quantity(2)
                .salesPrice(3000)
                .build();

        // Then
        assertEquals(100L, order.getOrderId());
        assertEquals(1L, order.getCustomerId());
        assertEquals(2L, order.getSellerId());
        assertEquals(3L, order.getProductId());
        assertEquals(2, order.getQuantity());
        assertEquals(3000, order.getSalesPrice());
    }

    @Test
    void setterShouldUpdateFieldsCorrectly() {
        // Given
        Order order = new Order();

        // When
        order.setOrderId(200L);
        order.setCustomerId(3L);
        order.setSellerId(4L);
        order.setProductId(5L);
        order.setQuantity(4);
        order.setSalesPrice(8000);

        // Then
        assertEquals(200L, order.getOrderId());
        assertEquals(3L, order.getCustomerId());
        assertEquals(4L, order.getSellerId());
        assertEquals(5L, order.getProductId());
        assertEquals(4, order.getQuantity());
        assertEquals(8000, order.getSalesPrice());
    }
}
