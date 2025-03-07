package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateProductDto {
    @NotNull
    @PositiveOrZero
    private Long categoryId;

    @NotNull
    @PositiveOrZero
    private Long sellerId;

    @NotBlank
    @Size(min = 2, max = 20, message = "name must be between 2 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "name must be number or character")
    private String name;

    @PositiveOrZero
    private int salesPrice;

    @PositiveOrZero
    private int stock;

    @Builder
    public CreateProductDto(Long categoryId, Long sellerId, String name, int salesPrice, int stock) {
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.name = name;
        this.salesPrice = salesPrice;
        this.stock = stock;
    }
}
