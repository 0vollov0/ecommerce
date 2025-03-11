package portfolio.ecommerce.order.response;

import lombok.*;
import portfolio.ecommerce.order.entity.Order;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private boolean result;
    private String message;

    @Builder
    public OrderResponse(Long orderId, boolean result, String message) {
        this.orderId = orderId;
        this.result = result;
        this.message = message;
    }
}
