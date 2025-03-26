package portfolio.ecommerce.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class PaymentResultDto {
    private boolean succeed;
    private Long orderId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentResultDto that = (PaymentResultDto) o;
        return succeed == that.succeed && Objects.equals(orderId, that.orderId);
    }
}
