package portfolio.ecommerce.order.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.order.config.RabbitConfig;
import portfolio.ecommerce.order.dto.RequestPaymentDto;

@Service
@RequiredArgsConstructor
public class PaymentRequestSender {
    private static final Logger log = LogManager.getLogger(PaymentRequestSender.class);
    private final RabbitTemplate rabbitTemplate;

    public void sendPaymentRequest(RequestPaymentDto dto) {
        rabbitTemplate.convertAndSend(RabbitConfig.PAYMENT_REQUEST_QUEUE, dto);
        log.info("📨 결제 요청 발행 완료: {}", dto);
    }
}