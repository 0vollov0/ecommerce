package portfolio.ecommerce.worker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderResultDto {
    private boolean succeed;
    private String productName;
    private int salesPrice;
    private int orderQuantity;
    private LocalDateTime orderDate;
}
