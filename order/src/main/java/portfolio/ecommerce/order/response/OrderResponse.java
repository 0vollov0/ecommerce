package portfolio.ecommerce.order.response;

import lombok.*;
import org.springframework.http.HttpStatus;
import portfolio.ecommerce.order.entity.Order;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String message;
    private HttpStatus status;

    @Builder
    public OrderResponse(HttpStatus status, Long orderId, String message) {
        this.orderId = orderId;
        this.message = message;
        this.status = status;
    }
}
