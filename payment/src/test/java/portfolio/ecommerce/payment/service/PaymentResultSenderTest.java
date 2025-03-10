package portfolio.ecommerce.payment.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import portfolio.ecommerce.payment.config.RabbitConfig;
import portfolio.ecommerce.payment.dto.PaymentResultDto;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentResultSenderTest {

    private static final Logger log = LogManager.getLogger(PaymentResultSenderTest.class);

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentResultSender paymentResultSender;

    @Test
    void sendPaymentResult_shouldSendMessageToQueue() {
        PaymentResultDto dto = new PaymentResultDto(true, 1L); // 필요한 필드 값 설정 가능

        paymentResultSender.sendPaymentResult(dto);

        verify(rabbitTemplate, times(1)).convertAndSend(RabbitConfig.PAYMENT_RESULT_QUEUE, dto);
        log.info("✅ 결제 결과 메시지 전송 테스트 성공: {}", dto);
    }
}
