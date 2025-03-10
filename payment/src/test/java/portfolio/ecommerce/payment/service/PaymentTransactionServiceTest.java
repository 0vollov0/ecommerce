package portfolio.ecommerce.payment.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolio.ecommerce.payment.entity.Customer;
import portfolio.ecommerce.payment.entity.Order;
import portfolio.ecommerce.payment.entity.Product;
import portfolio.ecommerce.payment.entity.StockLock;
import portfolio.ecommerce.payment.repository.CustomerRepository;
import portfolio.ecommerce.payment.repository.ProductRepository;
import portfolio.ecommerce.payment.repository.StockLockRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionServiceTest {

    @Mock
    private StockLockRepository stockLockRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    private PaymentTransactionService paymentTransactionService;

    private Product product;
    private Customer customer;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setStock(10);

        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setAmount(1000);

        StockLock stockLock = new StockLock();
        stockLock.setStockLockId(1L);
        stockLock.setProduct(product);
        stockLock.setQuantity(5);
        stockLock.setSalesPrice(500);
        stockLock.setOrder(Order.builder().customer(customer).build());

        Mockito.lenient().when(stockLockRepository.findById(1L)).thenReturn(Optional.of(stockLock));
        Mockito.lenient().when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.lenient().when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        paymentTransactionService = new PaymentTransactionService(stockLockRepository, productRepository, customerRepository);
    }

    @Test
    void processExpiredStock_shouldRestoreStockAndCustomerBalance() {
        paymentTransactionService.processExpiredStock(1L);

        assertEquals(15, product.getStock());
        assertEquals(1500, customer.getAmount());
        verify(productRepository, times(1)).save(product);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void processExpiredStock_shouldThrowExceptionWhenStockLockNotFound() {
        when(stockLockRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentTransactionService.processExpiredStock(2L));
    }
}
