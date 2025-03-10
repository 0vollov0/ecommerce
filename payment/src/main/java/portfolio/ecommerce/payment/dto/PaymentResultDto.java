package portfolio.ecommerce.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class PaymentResultDto {
    private boolean succeed;
    private Long orderId;
}
