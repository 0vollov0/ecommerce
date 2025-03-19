package portfolio.ecommerce.payment.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import portfolio.ecommerce.payment.dto.PaymentResultDto;
import portfolio.ecommerce.payment.dto.RequestPaymentDto;
import portfolio.ecommerce.payment.entity.Customer;
import portfolio.ecommerce.payment.entity.Order;
import portfolio.ecommerce.payment.entity.Product;
import portfolio.ecommerce.payment.entity.StockLock;
import portfolio.ecommerce.payment.repository.CustomerRepository;
import portfolio.ecommerce.payment.repository.OrderRepository;
import portfolio.ecommerce.payment.repository.ProductRepository;
import portfolio.ecommerce.payment.repository.StockLockRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private StockLockRepository stockLockRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentResultSender paymentResultSender;

    @Mock
    private PaymentTransactionService paymentTransactionService;

    @Mock
    private RedisService redisService;

    @Mock
    private UtilService utilService;

    private PaymentService paymentService;

    private RequestPaymentDto requestPaymentDto;
    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId(1L);
        product.setStock(10);

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setAmount(1000);

        StockLock stockLock = new StockLock();
        stockLock.setStockLockId(1L);
        stockLock.setProduct(product);
        stockLock.setQuantity(5);
        stockLock.setSalesPrice(500);
        stockLock.setOrder(Order.builder().customer(customer).build());

        order = new Order();
        order.setOrderId(1L);
        order.setState(0);
        order.setCustomer(customer);
        order.setProduct(product);

        requestPaymentDto = new RequestPaymentDto(
                1L, 1L, 1L, LocalDateTime.now().minusMinutes(1L), 1, 100
        );

        Mockito.lenient().when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.lenient().when(stockLockRepository.findById(1L)).thenReturn(Optional.of(stockLock));
        Mockito.lenient().when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.lenient().when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

//        paymentTransactionService = new PaymentTransactionService(stockLockRepository, productRepository, customerRepository);

        paymentService = new PaymentService(stockLockRepository, orderRepository, paymentResultSender, paymentTransactionService, redisService, utilService);
    }

    @Test
    void processPayment_shouldProcessSuccessfully_whenNotExpired() throws Exception {
        requestPaymentDto.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        paymentService.processPayment(requestPaymentDto);

        assertEquals(1, order.getState()); // 주문 상태가 정상 결제(1)로 변경
        verify(orderRepository, times(1)).save(order);
        verify(stockLockRepository, times(1)).deleteById(1L);
        verify(paymentResultSender, times(1)).sendPaymentResult(eq(new PaymentResultDto(true, 1L)));
    }

    @Test
    void processPayment_shouldProcessExpiredStock_whenExpired() throws Exception {
        requestPaymentDto.setExpiredAt(LocalDateTime.now().minusMinutes(5));

        paymentService.processPayment(requestPaymentDto);

        assertEquals(2, order.getState()); // 주문 상태가 만료(2)로 변경
        verify(paymentTransactionService, times(1)).processExpiredStock(1L);

        verify(orderRepository, times(1)).save(order);
        verify(stockLockRepository, times(1)).deleteById(1L);
        verify(paymentResultSender, times(1)).sendPaymentResult(eq(new PaymentResultDto(false, 1L)));
    }

    @Test
    void processPayment_shouldHandleExceptionAndSetOrderFailedState() throws Exception {
        requestPaymentDto.setStockLockId(2L);
        requestPaymentDto.setExpiredAt(LocalDateTime.MIN);
        paymentService.processPayment(requestPaymentDto);
        verify(orderRepository, times(1)).save(order);
        verify(paymentResultSender, times(1)).sendPaymentResult(eq(new PaymentResultDto(false, 1L)));
    }

    @Test
    void processPayment_shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.processPayment(requestPaymentDto));
    }
}
