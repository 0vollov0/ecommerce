package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCustomerDto {
    @NotBlank
    @Size(min = 2, max = 20, message = "name must be between 2 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "name must be number or character")
    private String name;

    @PositiveOrZero()
    private int amount;

    @Builder
    public CreateCustomerDto(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
