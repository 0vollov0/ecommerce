package portfolio.ecommerce.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import portfolio.ecommerce.order.dto.OrderDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.entity.*;
import portfolio.ecommerce.order.repository.CustomerRepository;
import portfolio.ecommerce.order.repository.OrderRepository;
import portfolio.ecommerce.order.repository.ProductRepository;
import portfolio.ecommerce.order.repository.StockLockRepository;
import portfolio.ecommerce.order.response.OrderResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StockLockRepository stockLockRepository;

    @Mock
    private PaymentRequestSender paymentRequestSender;

    @Mock
    private RedisService redisService;

    @Mock
    private UtilService utilService;

    private OrderService orderService;

    private Long productId = 1L;
    private Long customerId = 2L;
    private Long sellerId = 3L;
    private Long categoryId = 4L;
    private Long orderId = 5L;


    @BeforeEach
    void setUp() {
        orderService = new OrderService(productRepository, orderRepository, customerRepository, stockLockRepository, paymentRequestSender, redisService, utilService);
    }

    @Test
    void orderProduct() {
        OrderDto dto = new OrderDto(productId, customerId, 3);
        Customer customer = Customer.builder().customerId(customerId).name("TEST_CUSTOMER").amount(10000).build();
        Product product = Product.builder().productId(productId).sellerId(sellerId).stock(5).salesPrice(2000).build();
        Order order = Order.builder().orderId(orderId).customerId(customer.getCustomerId()).sellerId(product.getSellerId()).productId(product.getProductId()).quantity(dto.getQuantity()).salesPrice(6000).build();
        StockLock stockLock = StockLock.builder().orderId(order.getOrderId()).productId(product.getProductId()).salesPrice(6000).quantity(dto.getQuantity()).expiredAt(LocalDateTime.now().plusMinutes(3)).build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdForUpdate(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(stockLockRepository.save(any(StockLock.class))).thenReturn(stockLock);
        when(utilService.generateHash(String.valueOf(dto.getCustomer_id() + dto.getProduct_id() + dto.getQuantity()))).thenReturn("TEST_HASH_CODE");
        when(redisService.lockData("TEST_HASH_CODE", "processed", 1)).thenReturn(true);

        OrderResponse response = orderService.order(dto);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Your order has been proceed", response.getMessage());
        assertEquals(4000, customer.getAmount());
        assertEquals(2, product.getStock());
        verify(paymentRequestSender, times(1)).sendPaymentRequest(any());
    }

    @Test
    void orderProduct_NotEnoughStock() {
        OrderDto dto = new OrderDto(productId, customerId, 10);
        Product product = Product.builder().productId(productId).categoryId(categoryId).sellerId(sellerId).stock(1).salesPrice(2000).name("TEST_PRODUCT").build();

        when(utilService.generateHash(String.valueOf(dto.getCustomer_id() + dto.getProduct_id() + dto.getQuantity()))).thenReturn("TEST_HASH_CODE");
        when(redisService.lockData("TEST_HASH_CODE", "processed", 1)).thenReturn(true);
        when(productRepository.findByIdForUpdate(productId)).thenReturn(Optional.of(product));

        OrderResponse response = orderService.order(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Not enough stock to order", response.getMessage());
    }

    @Test
    void orderProduct_NotEnoughAmount() {
        OrderDto dto = new OrderDto(productId, customerId, 10);
        Product product = Product.builder().productId(productId).sellerId(sellerId).stock(10).salesPrice(2000).build();
        Customer customer = Customer.builder().customerId(customerId).name("TEST_CUSTOMER").amount(5000).build();

        when(utilService.generateHash(String.valueOf(dto.getCustomer_id() + dto.getProduct_id() + dto.getQuantity()))).thenReturn("TEST_HASH_CODE");
        when(redisService.lockData("TEST_HASH_CODE", "processed", 1)).thenReturn(true);
        when(productRepository.findByIdForUpdate(productId)).thenReturn(Optional.of(product));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        OrderResponse response = orderService.order(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Not enough amount to order", response.getMessage());
    }

    @Test
    void findOrders() {
        RequestPagingDto dto = new RequestPagingDto(0, 10);
        Page<Order> page = new PageImpl<>(Collections.singletonList(new Order()));

        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Order> result = orderService.find(dto);


        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(orderRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void findOrderById() {
        Order order = Order.builder().orderId(1L).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderService.findById(1L);

        assertTrue(foundOrder.isPresent());
        assertEquals(1L, foundOrder.get().getOrderId());
        verify(orderRepository, times(1)).findById(1L);
    }
}