package portfolio.ecommerce.payment.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void orderBuilderShouldCreateObjectCorrectly() {
        // Given
        Customer customer = new Customer(1L, "John Doe", 5000, false);
        Seller seller = new Seller(2L, "Amazon", false);
        Product product = new Product(3L, "Laptop", 1500, 10, 1L, 2L, false, null, null);

        Order order = Order.builder()
                .orderId(100L)
                .customerId(1L)
                .sellerId(2L)
                .productId(3L)
                .quantity(2)
                .salesPrice(3000)
                .customer(customer)
                .seller(seller)
                .product(product)
                .build();

        // Then
        assertEquals(100L, order.getOrderId());
        assertEquals(1L, order.getCustomerId());
        assertEquals(2L, order.getSellerId());
        assertEquals(3L, order.getProductId());
        assertEquals(2, order.getQuantity());
        assertEquals(3000, order.getSalesPrice());
        assertNotNull(order.getCustomer());
        assertNotNull(order.getSeller());
        assertNotNull(order.getProduct());
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
