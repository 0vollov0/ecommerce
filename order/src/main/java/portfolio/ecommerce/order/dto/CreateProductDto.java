package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductDto {
    @NotNull
    @PositiveOrZero
    private Long categoryId;

    @NotNull
    @PositiveOrZero
    private Long sellerId;

    @NotBlank
    private String name;

    @PositiveOrZero
    private int salesPrice;

    @PositiveOrZero
    private int stock;
}
