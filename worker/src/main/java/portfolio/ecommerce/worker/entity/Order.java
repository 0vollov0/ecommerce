package portfolio.ecommerce.worker.entity;

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

    @ManyToOne()
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne()
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @ManyToOne()
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
    public Order(Customer customer, Seller seller, Product product, int quantity, int salesPrice) {
        this.customer = customer;
        this.seller = seller;
        this.product = product;
        this.quantity = quantity;
        this.salesPrice = salesPrice;
    }
}