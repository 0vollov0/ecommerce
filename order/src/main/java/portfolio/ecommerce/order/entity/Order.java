package portfolio.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "`order`")
@ToString
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long orderId;

    // ✅ customer_id, seller_id, product_id 값만 조회하는 getter 추가
    // ✅ customer_id 값을 직접 저장하여 추가적인 SELECT 방지
    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ LAZY 설정으로 JOIN 방지
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // ✅ seller_id 값을 직접 저장하여 추가적인 SELECT 방지
    @Column(name = "seller_id", insertable = false, updatable = false)
    private Long sellerId;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ LAZY 설정으로 JOIN 방지
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    // ✅ product_id 값을 직접 저장하여 추가적인 SELECT 방지
    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ LAZY 설정으로 JOIN 방지
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int salesPrice;

    // 대기중(0), 결제완료(1), 결제실패(2)
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private int state;

    @Builder
    public Order(Long orderId, Long customerId, Long sellerId, Long productId, int quantity, int salesPrice) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.quantity = quantity;
        this.salesPrice = salesPrice;

        // 연관 엔티티는 null로 설정 (Lazy 로딩 방지)
        this.customer = null;
        this.seller = null;
        this.product = null;
    }

    @PrePersist
    @PreUpdate
    private void syncIds() {
        if (customer != null) {
            this.customerId = customer.getCustomerId();
        }
        if (seller != null) {
            this.sellerId = seller.getSellerId();
        }
        if (product != null) {
            this.productId = product.getProductId();
        }
    }
}