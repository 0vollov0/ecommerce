package portfolio.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import portfolio.ecommerce.order.dto.RequestPaymentDto;

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

    // ✅ order_id 값을 직접 저장하여 추가적인 SELECT 방지
    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ LAZY 설정으로 JOIN 방지
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // ✅ product_id 값을 직접 저장하여 추가적인 SELECT 방지
    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ LAZY 설정으로 JOIN 방지
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int salesPrice;

    @Builder
    public StockLock(Long stockLockId, Long orderId, Long productId, int salesPrice, int quantity, LocalDateTime expiredAt) {
        this.stockLockId = stockLockId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.expiredAt = expiredAt;
        this.salesPrice = salesPrice;

        // 연관 엔티티는 null로 설정 (Lazy 로딩 방지)
        this.order = null;
        this.product = null;
    }

    public RequestPaymentDto toPaymentRequestDto() {
        return new RequestPaymentDto(stockLockId, orderId, productId, expiredAt, quantity, salesPrice);
    }
}