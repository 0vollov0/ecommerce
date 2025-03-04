package portfolio.ecommerce.order.entity;

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
    private Long order_id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int total_price;

    // 대기중, 재고확보, 결제대기, 결제완료, 결제실패, 취소
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private int state;

    @Builder
    public Order(Customer customer, Seller seller, Product product, int quantity, int total_price) {
        this.customer = customer;
        this.seller = seller;
        this.product = product;
        this.quantity = quantity;
        this.total_price = total_price;
    }
}