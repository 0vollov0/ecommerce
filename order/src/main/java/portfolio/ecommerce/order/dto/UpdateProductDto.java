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
public class UpdateProductDto {
    @PositiveOrZero
    private Long categoryId;

    @NotBlank
    @Size(min = 2, max = 20, message = "name must be between 2 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "name must be number or character")
    private String name;

    @PositiveOrZero
    private int salesPrice;

    @PositiveOrZero
    private int stock;

    @Builder
    public UpdateProductDto(Long categoryId, String name, int salesPrice, int stock) {
        this.categoryId = categoryId;
        this.name = name;
        this.salesPrice = salesPrice;
        this.stock = stock;
    }
}
