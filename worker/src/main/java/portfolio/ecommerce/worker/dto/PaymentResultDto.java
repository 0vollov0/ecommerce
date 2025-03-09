package portfolio.ecommerce.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResultDto {
    private boolean succeed;
    private Long orderId;
}
