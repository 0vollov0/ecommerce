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
public class StockLock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long stockLockId;

    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int salesPrice;

    @Builder
    public StockLock(Long stockLockId, Long orderId, Long productId, int salesPrice, int quantity, LocalDateTime expiredAt, Order order, Product product) {
        this.stockLockId = stockLockId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.expiredAt = expiredAt;
        this.salesPrice = salesPrice;

        this.order = order;
        this.product = product;
    }

    public RequestPaymentDto toPaymentRequestDto() {
        return new RequestPaymentDto(stockLockId, orderId, productId, expiredAt, quantity, salesPrice);
    }

    @PrePersist
    @PreUpdate
    private void syncIds() {
        if (order != null) {
            this.orderId = order.getOrderId();
        }
    }
}