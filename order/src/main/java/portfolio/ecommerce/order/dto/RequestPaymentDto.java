package portfolio.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RequestPaymentDto {
    private Long stockLockId;
    private Long orderId;
    private Long productId;
    private LocalDateTime expiredAt;
    private int quantity;
    private int salesPrice;
}
