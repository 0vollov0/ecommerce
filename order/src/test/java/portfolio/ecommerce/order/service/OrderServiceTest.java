package portfolio.ecommerce.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
import java.util.Objects;
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

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(productRepository, orderRepository, customerRepository, stockLockRepository, paymentRequestSender);
    }

    @Test
    void orderProduct() {
        OrderDto dto = new OrderDto(1L, 2L, 3);
        Customer customer = Customer.builder().customerId(2L).name("TEST_CUSTOMER").amount(10000).build();
        Product product = Product.builder().productId(1L).seller(Seller.builder().name("TEST_SELLER").build()).stock(5).salesPrice(2000).build();
        Order order = Order.builder().customer(customer).seller(product.getSeller()).product(product).quantity(dto.getQuantity()).salesPrice(6000).build();
        StockLock stockLock = StockLock.builder().order(order).product(product).salesPrice(6000).quantity(dto.getQuantity()).expiredAt(LocalDateTime.now().plusMinutes(3)).build();

        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(stockLockRepository.save(any(StockLock.class))).thenReturn(stockLock);

        ResponseEntity<OrderResponse> response = orderService.order(dto);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Your order has been proceed", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(4000, customer.getAmount());
        assertEquals(2, product.getStock());
        verify(paymentRequestSender, times(1)).sendPaymentRequest(any());
    }

    @Test
    void orderProduct_NotEnoughStock() {
        OrderDto dto = new OrderDto(1L, 2L, 10);
        Product product = Product.builder().productId(1L).seller(Seller.builder().name("TEST_SELLER").build()).stock(5).salesPrice(2000).build();
        Customer customer = Customer.builder().customerId(2L).name("TEST_CUSTOMER").amount(10000).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));

        ResponseEntity<OrderResponse> response = orderService.order(dto);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Not enough stock to order", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void orderProduct_NotEnoughAmount() {
        OrderDto dto = new OrderDto(1L, 2L, 10);
        Product product = Product.builder().productId(1L).seller(Seller.builder().name("TEST_SELLER").build()).stock(10).salesPrice(2000).build();
        Customer customer = Customer.builder().customerId(2L).name("TEST_CUSTOMER").amount(5000).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));

        ResponseEntity<OrderResponse> response = orderService.order(dto);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Not enough amount to order", Objects.requireNonNull(response.getBody()).getMessage());
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