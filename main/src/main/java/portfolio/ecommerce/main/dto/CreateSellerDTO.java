package portfolio.ecommerce.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSellerDTO {
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;
}
