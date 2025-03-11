package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @Positive
    private Long product_id;

    @Positive
    private Long customer_id;

    @Positive
    private int quantity;
}
