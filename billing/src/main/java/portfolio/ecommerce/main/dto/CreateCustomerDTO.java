package portfolio.ecommerce.main.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateCustomerDTO {
    @NotBlank
    @Length(max = 255)
    private String name;
}
