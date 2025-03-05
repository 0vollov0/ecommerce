package portfolio.ecommerce.order.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderResponse {
    private boolean result;
    private String message;
}
