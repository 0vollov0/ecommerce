package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSellerDto {
    @NotBlank
    @Size(min = 2, max = 20, message = "name must be between 2 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "name must be number or character")
    private String name;

    @Builder
    public UpdateSellerDto(String name) {
        this.name = name;
    }
}
