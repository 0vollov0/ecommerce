package portfolio.ecommerce.payment.integration;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import portfolio.ecommerce.payment.config.RabbitConfig;
import portfolio.ecommerce.payment.dto.PaymentResultDto;
import portfolio.ecommerce.payment.dto.RequestPaymentDto;
import portfolio.ecommerce.payment.entity.Order;
import portfolio.ecommerce.payment.entity.StockLock;
import portfolio.ecommerce.payment.repository.OrderRepository;
import portfolio.ecommerce.payment.repository.StockLockRepository;
import portfolio.ecommerce.payment.service.PaymentService;
import portfolio.ecommerce.payment.service.PaymentTransactionService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = "/data/payment.sql")  // 테스트용 데이터 추가
@Sql(scripts = "/data/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // 테스트 후 데이터 삭제
class PaymentServiceIntegrationTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockLockRepository stockLockRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Mock
    private PaymentTransactionService paymentTransactionService;

    private static final Long existingId = 99999L;

    @BeforeEach
    void setUp() {
        assertTrue(orderRepository.existsById(existingId), "Order should be exist");
        assertTrue(stockLockRepository.existsById(existingId), "StockLock should be exist");
    }

    @Test
    void processPayment_shouldProcessSuccessfully_whenNotExpired() throws Exception {
        StockLock stockLock = stockLockRepository.findById(existingId).orElseThrow(EntityNotFoundException::new);
        RequestPaymentDto requestPaymentDto = stockLock.toPaymentRequestDto();

        paymentService.processPayment(requestPaymentDto);

        Order updatedOrder = orderRepository.findById(existingId).orElseThrow(EntityNotFoundException::new);
        assertThat(updatedOrder.getState()).isEqualTo(1);
        assertThat(stockLockRepository.findById(existingId)).isEmpty();
        PaymentResultDto paymentResultDto = (PaymentResultDto) rabbitTemplate.receiveAndConvert(RabbitConfig.PAYMENT_RESULT_QUEUE);
        PaymentResultDto expectedResultDto = new PaymentResultDto(true, updatedOrder.getOrderId());
        assertThat(paymentResultDto).isInstanceOf(PaymentResultDto.class);
        assertThat(paymentResultDto)
                .extracting(PaymentResultDto::getOrderId, PaymentResultDto::isSucceed)
                .containsExactly(expectedResultDto.getOrderId(), expectedResultDto.isSucceed());
    }

    @Test
    void processPayment_shouldHandleExpiredStock() throws Exception {
        Long expiredId = 99998L;
        StockLock stockLock = stockLockRepository.findById(expiredId).orElseThrow(EntityNotFoundException::new);
        RequestPaymentDto requestPaymentDto = stockLock.toPaymentRequestDto();

        paymentService.processPayment(requestPaymentDto);

        Order updatedOrder = orderRepository.findById(existingId).orElseThrow(EntityNotFoundException::new);
        assertThat(updatedOrder.getState()).isEqualTo(2);
        assertThat(stockLockRepository.findById(expiredId)).isEmpty();
        PaymentResultDto paymentResultDto = (PaymentResultDto) rabbitTemplate.receiveAndConvert(RabbitConfig.PAYMENT_RESULT_QUEUE);
        PaymentResultDto expectedResultDto = new PaymentResultDto(true, updatedOrder.getOrderId());
        assertThat(paymentResultDto).isInstanceOf(PaymentResultDto.class);
        assertThat(paymentResultDto)
                .extracting(PaymentResultDto::getOrderId, PaymentResultDto::isSucceed)
                .containsExactly(expectedResultDto.getOrderId(), expectedResultDto.isSucceed());
    }
}
