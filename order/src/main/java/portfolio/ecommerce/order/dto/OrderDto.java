package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
    @Positive
    private Long product_id;

    @Positive
    private Long customer_id;

    @Positive
    private int quantity;
}
