package portfolio.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class RequestPaymentDto {
    private Long stockLockId;
    private Long orderId;
    private Long productId;
    private LocalDateTime expiredAt;
    private int quantity;
    private int salesPrice;
}
