package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSellerDto {
    @NotBlank
    private String name;

    @Builder
    public UpdateSellerDto(String name) {
        this.name = name;
    }
}
