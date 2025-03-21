package portfolio.ecommerce.payment.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import portfolio.ecommerce.payment.dto.RequestPaymentDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StockLock extends portfolio.ecommerce.payment.entity.BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long stockLockId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private  Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private portfolio.ecommerce.payment.entity.Product product;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int salesPrice;

    @Builder
    public StockLock(Order order, portfolio.ecommerce.payment.entity.Product product, int salesPrice, int quantity, LocalDateTime expiredAt) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.expiredAt = expiredAt;
        this.salesPrice = salesPrice;
    }

    public RequestPaymentDto toPaymentRequestDto() {
        return new RequestPaymentDto(stockLockId, order.getOrderId(), product.getProductId(), expiredAt, quantity, salesPrice);
    }
}