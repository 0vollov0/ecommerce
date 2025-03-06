package portfolio.ecommerce.payment.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.payment.config.RabbitConfig;
import portfolio.ecommerce.payment.dto.PaymentResultDto;
import portfolio.ecommerce.payment.dto.RequestPaymentDto;
import portfolio.ecommerce.payment.entity.Order;
import portfolio.ecommerce.payment.repository.OrderRepository;
import portfolio.ecommerce.payment.repository.StockLockRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private static final Logger log = LogManager.getLogger(PaymentService.class);
    private final StockLockRepository stockLockRepository;
    private final OrderRepository orderRepository;
    private final PaymentResultSender paymentResultSender;
    private final PaymentTransactionService paymentTransactionService;

    @Transactional
    @RabbitListener(queues = RabbitConfig.PAYMENT_REQUEST_QUEUE)
    public void processPayment(RequestPaymentDto dto) throws Exception {
        try {
            LocalDateTime now = LocalDateTime.now();
            boolean isExpired = dto.getExpiredAt().isAfter(now);
            if (isExpired) paymentTransactionService.processExpireStock(dto.getStockLockId());
            Order order = orderRepository.findById(dto.getOrderId()).orElseThrow(EntityNotFoundException::new);
            order.setState(isExpired ? 2 : 1);
            orderRepository.save(order);
            stockLockRepository.deleteById(dto.getStockLockId());
            PaymentResultDto resultDto = new PaymentResultDto(true, dto.getOrderId());
            paymentResultSender.sendPaymentResult(resultDto);
        } catch (Exception e) {
            Order order = orderRepository.findById(dto.getOrderId()).orElseThrow(EntityNotFoundException::new);
            order.setState(2);
            orderRepository.save(order);
            PaymentResultDto resultDto = new PaymentResultDto(false, dto.getOrderId());
            paymentResultSender.sendPaymentResult(resultDto);
            throw e;
        }
    }
}
