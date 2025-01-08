package portfolio.ecommerce.main.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductDTO {
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    @NotBlank
    @PositiveOrZero
    private int price;

    @NotBlank
    @PositiveOrZero
    private int quantity;

    @NotBlank
    @PositiveOrZero
    private Long category_id;
}
