package portfolio.ecommerce.payment.entity;

import org.junit.jupiter.api.Test;
import portfolio.ecommerce.payment.dto.RequestPaymentDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StockLockTest {

    @Test
    void stockLockBuilderShouldCreateObjectCorrectly() {
        // Given
        Order order = new Order(1L, 1L, 2L, 3L, 2, 3000, null, null, null);
        Product product = new Product(3L, "Laptop", 1500, 10, 1L, 2L, false, null, null);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(30);

        StockLock stockLock = StockLock.builder()
                .stockLockId(100L)
                .orderId(1L)
                .productId(3L)
                .quantity(2)
                .salesPrice(3000)
                .expiredAt(expiryTime)
                .order(order)
                .product(product)
                .build();

        // Then
        assertEquals(100L, stockLock.getStockLockId());
        assertEquals(1L, stockLock.getOrderId());
        assertEquals(3L, stockLock.getProductId());
        assertEquals(2, stockLock.getQuantity());
        assertEquals(3000, stockLock.getSalesPrice());
        assertEquals(expiryTime, stockLock.getExpiredAt());
        assertNotNull(stockLock.getOrder());
        assertNotNull(stockLock.getProduct());
    }

    @Test
    void setterShouldUpdateFieldsCorrectly() {
        // Given
        StockLock stockLock = new StockLock();

        // When
        stockLock.setStockLockId(200L);
        stockLock.setOrderId(2L);
        stockLock.setProductId(5L);
        stockLock.setQuantity(5);
        stockLock.setSalesPrice(7500);
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(1);
        stockLock.setExpiredAt(expiryTime);

        // Then
        assertEquals(200L, stockLock.getStockLockId());
        assertEquals(2L, stockLock.getOrderId());
        assertEquals(5L, stockLock.getProductId());
        assertEquals(5, stockLock.getQuantity());
        assertEquals(7500, stockLock.getSalesPrice());
        assertEquals(expiryTime, stockLock.getExpiredAt());
    }

    @Test
    void toPaymentRequestDtoShouldReturnCorrectDto() {
        // Given
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(20);
        StockLock stockLock = StockLock.builder()
                .stockLockId(300L)
                .orderId(4L)
                .productId(6L)
                .quantity(3)
                .salesPrice(4500)
                .expiredAt(expiryTime)
                .build();

        // When
        RequestPaymentDto dto = stockLock.toPaymentRequestDto();

        // Then
        assertEquals(300L, dto.getStockLockId());
        assertEquals(4L, dto.getOrderId());
        assertEquals(6L, dto.getProductId());
        assertEquals(3, dto.getQuantity());
        assertEquals(4500, dto.getSalesPrice());
        assertEquals(expiryTime, dto.getExpiredAt());
    }
}
