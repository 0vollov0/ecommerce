package portfolio.ecommerce.worker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import portfolio.ecommerce.worker.dto.PaymentResultDto;
import portfolio.ecommerce.worker.entity.Customer;
import portfolio.ecommerce.worker.entity.Order;
import portfolio.ecommerce.worker.entity.Product;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private EntityManager em;

    @Mock
    private SseService sseService;

    @Mock
    private TypedQuery<Order> query;

    private PaymentService paymentService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Long existingCustomerId = 1L;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(sseService);
        paymentService.objectMapper = objectMapper;
        paymentService.em = em;
    }

    @Test
    void testProcessPayment_SuccessfulPayment() throws Exception {
        PaymentResultDto paymentResultDto = new PaymentResultDto(true, existingCustomerId);

        Order mockOrder = new Order();
        mockOrder.setOrderId(existingCustomerId);
        mockOrder.setQuantity(10);
        mockOrder.setSalesPrice(1000);
        Product mockProduct = new Product();
        mockProduct.setName("Test Product");
        mockOrder.setProduct(mockProduct);

        Customer mockCustomer = Customer.builder().name("Test Customer").amount(1000).build();
        mockCustomer.setCustomerId(existingCustomerId);
        mockOrder.setCustomer(mockCustomer);

        SseEmitter mockSseEmitter = mock(SseEmitter.class);

        when(em.createQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(mockOrder);
        when(sseService.getEmitter(existingCustomerId)).thenReturn(mockSseEmitter);

        paymentService.processPayment(paymentResultDto);

        verify(mockSseEmitter, times(1)).send(any(SseEmitter.SseEventBuilder.class), any(MediaType.class));
    }

    @Test
    void testProcessPayment_FailedPayment() throws Exception {
        PaymentResultDto paymentResultDto = new PaymentResultDto(false, existingCustomerId);

        Order mockOrder = new Order();
        mockOrder.setOrderId(existingCustomerId);
        mockOrder.setQuantity(10);
        mockOrder.setSalesPrice(1000);
        Product mockProduct = Product.builder().name("Test Product").build();
        mockOrder.setProduct(mockProduct);
        Customer mockCustomer = Customer.builder().name("Test Customer").amount(1000).build();
        mockCustomer.setCustomerId(existingCustomerId);
        mockOrder.setCustomer(mockCustomer);

        SseEmitter mockSseEmitter = mock(SseEmitter.class);

        when(em.createQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(mockOrder);
        when(sseService.getEmitter(existingCustomerId)).thenReturn(mockSseEmitter);

        paymentService.processPayment(paymentResultDto);

        verify(mockSseEmitter, times(1)).send(any(SseEmitter.SseEventBuilder.class), any(MediaType.class));
    }

    @Test
    void testProcessPayment_NoSseEmitter() throws Exception {
        Long nonExistingCustomerId = 999L;
        PaymentResultDto paymentResultDto = new PaymentResultDto(true, nonExistingCustomerId);

        Order mockOrder = new Order();
        mockOrder.setOrderId(nonExistingCustomerId);
        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(nonExistingCustomerId);
        mockOrder.setCustomer(mockCustomer);

        when(em.createQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(mockOrder);
        when(sseService.getEmitter(nonExistingCustomerId)).thenReturn(null);

        paymentService.processPayment(paymentResultDto);

        verify(sseService, times(1)).getEmitter(nonExistingCustomerId);
    }
}