package portfolio.ecommerce.payment.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.payment.config.RabbitConfig;
import portfolio.ecommerce.payment.dto.PaymentResultDto;

@Service
@RequiredArgsConstructor
public class PaymentResultSender {
    private static final Logger log = LogManager.getLogger(PaymentResultSender.class);
    private final RabbitTemplate rabbitTemplate;

    public void sendPaymentResult(PaymentResultDto dto) {
        rabbitTemplate.convertAndSend(RabbitConfig.PAYMENT_RESULT_QUEUE, dto);
        log.info("📨 결제 결과 전송 : {}", dto);
    }
}