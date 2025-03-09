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

    @InjectMocks
    private PaymentService paymentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        paymentService.objectMapper = objectMapper;
    }

    @Test
    void testProcessPayment_SuccessfulPayment() throws Exception {
        PaymentResultDto paymentResultDto = new PaymentResultDto(true, 1L);

        Order mockOrder = new Order();
        mockOrder.setOrderId(1L);
        mockOrder.setQuantity(10);
        mockOrder.setSalesPrice(1000);
        Product mockProduct = new Product();
        mockProduct.setName("Test Product");
        mockOrder.setProduct(mockProduct);

        Customer mockCustomer = Customer.builder().name("Test Customer").amount(1000).build();
        mockCustomer.setCustomerId(1L);
        mockOrder.setCustomer(mockCustomer);

        SseEmitter mockSseEmitter = mock(SseEmitter.class);

//        Map<Long, SseEmitter> clients = new HashMap<>();
//        clients.put(1L, mockSseEmitter);

        // When (Mock 설정)
        when(em.createQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(mockOrder);
        when(sseService.getEmitter(1L)).thenReturn(mockSseEmitter);

        paymentService.processPayment(paymentResultDto);

        verify(mockSseEmitter, times(1)).send(any(SseEmitter.SseEventBuilder.class), any(MediaType.class));
    }

    @Test
    void testProcessPayment_FailedPayment() throws Exception {
        PaymentResultDto paymentResultDto = new PaymentResultDto(false, 2L);

        Order mockOrder = new Order();
        mockOrder.setOrderId(2L);
        mockOrder.setQuantity(10);
        mockOrder.setSalesPrice(1000);
        Product mockProduct = Product.builder().name("Test Product").build();
        mockOrder.setProduct(mockProduct);
        Customer mockCustomer = Customer.builder().name("Test Customer").amount(1000).build();
        mockCustomer.setCustomerId(2L);
        mockOrder.setCustomer(mockCustomer);

        SseEmitter mockSseEmitter = mock(SseEmitter.class);

//        Map<Long, SseEmitter> clients = new HashMap<>();
//        clients.put(2L, mockSseEmitter);

        when(em.createQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(mockOrder);
        when(sseService.getEmitter(2L)).thenReturn(mockSseEmitter);

        paymentService.processPayment(paymentResultDto);

        verify(mockSseEmitter, times(1)).send(any(SseEmitter.SseEventBuilder.class), any(MediaType.class));
    }

    @Test
    void testProcessPayment_NoSseEmitter() throws Exception {
        // Given
        PaymentResultDto paymentResultDto = new PaymentResultDto(true, 3L);

        Order mockOrder = new Order();
        mockOrder.setOrderId(3L);
        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(3L);
        mockOrder.setCustomer(mockCustomer);

        // When
        when(em.createQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(mockOrder);
        when(sseService.getEmitter(3L)).thenReturn(null);


        // Then
        paymentService.processPayment(paymentResultDto);

        verify(sseService, times(1)).getEmitter(3L);
    }
}