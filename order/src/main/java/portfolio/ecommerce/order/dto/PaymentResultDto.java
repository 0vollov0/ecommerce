package portfolio.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResultDto {
    private boolean succeed;
    private Long orderId;
}
