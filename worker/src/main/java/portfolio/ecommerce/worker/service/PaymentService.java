package portfolio.ecommerce.worker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import portfolio.ecommerce.worker.config.RabbitConfig;
import portfolio.ecommerce.worker.dto.OrderResultDto;
import portfolio.ecommerce.worker.dto.PaymentResultDto;
import portfolio.ecommerce.worker.entity.Order;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentService {
    @PersistenceContext
    EntityManager em;
    private final SseService sseService;
    ObjectMapper objectMapper = new ObjectMapper();


    @Transactional
    @RabbitListener(queues = RabbitConfig.PAYMENT_RESULT_QUEUE)
    public void processPayment(PaymentResultDto dto) throws Exception {
        List<Order> orders = em.createQuery(
                        "SELECT o FROM Order o WHERE o.orderId = :orderId", Order.class)
                .setParameter("orderId", dto.getOrderId())
                .getResultList();

        Order order = orders.isEmpty() ? null : orders.get(0);
        if (order == null) return;
        SseEmitter sseEmitter = sseService.getEmitter(order.getCustomer().getCustomerId());

        if (sseEmitter == null) return;
        OrderResultDto orderResultDto = new OrderResultDto();
        orderResultDto.setSucceed(dto.isSucceed());
        if (!orderResultDto.isSucceed())
            sseEmitter.send(
                SseEmitter.event().data(objectMapper.writeValueAsString(orderResultDto)),
                MediaType.TEXT_EVENT_STREAM
            );
        else{

            orderResultDto.setOrderQuantity(order.getQuantity());
            orderResultDto.setProductName(order.getProduct().getName());
            orderResultDto.setSalesPrice(order.getSalesPrice());
            orderResultDto.setOrderDate(order.getCreatedAt());
            sseEmitter.send(
                    SseEmitter.event().data(objectMapper.writeValueAsString(orderResultDto)),
                    MediaType.TEXT_EVENT_STREAM
            );
        }
    }
}
