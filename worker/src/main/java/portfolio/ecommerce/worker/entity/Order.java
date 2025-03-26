package portfolio.ecommerce.worker.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "`order`")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long orderId;

    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "seller_id", insertable = false, updatable = false)
    private Long sellerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int salesPrice;

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

        this.customer = null;
        this.seller = null;
        this.product = null;
    }
}