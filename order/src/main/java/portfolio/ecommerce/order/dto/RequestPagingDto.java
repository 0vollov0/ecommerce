package portfolio.ecommerce.order.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class RequestPagingDto {
    @Min(value = 0, message = "page should be positive number including 0")
    private int page;
    @Min(value = 1, message = "pageSize should be upper than 0")
    private int pageSize;
}
