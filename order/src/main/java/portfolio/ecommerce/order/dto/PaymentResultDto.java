package portfolio.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public class PaymentResultDto {
    private boolean succeed;
    private Long orderId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentResultDto that = (PaymentResultDto) o;
        return succeed == that.succeed && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(succeed, orderId);
    }
}
