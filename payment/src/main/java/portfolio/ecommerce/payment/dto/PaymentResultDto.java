package portfolio.ecommerce.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResultDto {
    private boolean result;
    private Long orderId;
//    private Long productId;
//    private String productName;
//    private int quantity;
//    private int orderPrice;
//    private int salesPrice;
}
