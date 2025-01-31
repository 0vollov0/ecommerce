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

    @PositiveOrZero
    private int price;

    @PositiveOrZero
    private int quantity;

    @PositiveOrZero
    private Long category_id;
}
