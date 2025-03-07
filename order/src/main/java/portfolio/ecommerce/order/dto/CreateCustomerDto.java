package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerDto {
    @NotBlank
    private String name;

    @PositiveOrZero()
    private int amount;
}
