package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCustomerDto {
    @NotBlank
    private String name;

    @PositiveOrZero
    private int amount;

    @Builder
    public UpdateCustomerDto(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
