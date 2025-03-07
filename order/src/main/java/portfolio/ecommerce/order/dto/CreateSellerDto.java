package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSellerDto {
    @NotBlank
    private String name;
}
