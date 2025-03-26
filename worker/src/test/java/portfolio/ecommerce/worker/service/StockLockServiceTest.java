package portfolio.ecommerce.worker.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolio.ecommerce.worker.entity.Customer;
import portfolio.ecommerce.worker.entity.Order;
import portfolio.ecommerce.worker.entity.Product;
import portfolio.ecommerce.worker.entity.StockLock;
import portfolio.ecommerce.worker.repository.StockLockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StockLockServiceTest {

    @InjectMocks
    private StockLockService stockLockService;

    @Mock
    private StockLockRepository stockLockRepository;

    @Mock
    private EntityManager entityManager;

    private StockLock stockLock;
    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John Doe");
        customer.setAmount(5000);

        product = new Product();
        product.setProductId(1L);
        product.setName("Laptop");
        product.setStock(10);

        Order order = new Order();
        order.setOrderId(1L);
        order.setCustomer(customer);
        order.setProduct(product);
        order.setSalesPrice(1000);
        order.setQuantity(2);

        stockLock = new StockLock();
        stockLock.setStockLockId(1L);
        stockLock.setExpiredAt(LocalDateTime.now().minusMinutes(10));
        stockLock.setOrder(order);
        stockLock.setProduct(product);
        stockLock.setSalesPrice(order.getSalesPrice());
        stockLock.setQuantity(order.getQuantity());
    }

    @Test
    void shouldProcessExpiredStockLocks() {
        when(stockLockRepository.findAllExpired(any(LocalDateTime.class)))
                .thenReturn(List.of(stockLock));

        int processedCount = stockLockService.processExpiredStockLocks();

        assertEquals(6000, customer.getAmount());
        assertEquals(12, product.getStock());
        assertEquals(1, processedCount);

        verify(entityManager, times(1)).remove(stockLock);
    }

    @Test
    void shouldNotProcessIfNoExpiredStockLocks() {
        when(stockLockRepository.findAllExpired(any(LocalDateTime.class)))
                .thenReturn(List.of());

        int processedCount = stockLockService.processExpiredStockLocks();

        assertEquals(0, processedCount);

        verify(entityManager, never()).remove(any());
    }
}
