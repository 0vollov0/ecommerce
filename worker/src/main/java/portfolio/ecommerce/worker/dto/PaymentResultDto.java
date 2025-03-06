package portfolio.ecommerce.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResultDto {
    private boolean result;
    private Long orderId;
}
