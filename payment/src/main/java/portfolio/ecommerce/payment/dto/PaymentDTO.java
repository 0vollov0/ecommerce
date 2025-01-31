package portfolio.ecommerce.payment.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
    @Positive
    private Long product_id;

    @Positive
    private Long customer_id;

    @Positive
    private int quantity;
}
