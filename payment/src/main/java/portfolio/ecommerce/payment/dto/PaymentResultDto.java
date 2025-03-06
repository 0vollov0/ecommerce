package portfolio.ecommerce.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResultDto {
    private boolean succeed;
    private Long orderId;
}
