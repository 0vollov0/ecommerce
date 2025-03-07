package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProductDto {
    @PositiveOrZero
    private Long categoryId;

    @NotBlank
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
