package portfolio.ecommerce.payment.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long reservation_id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

//    @Column(nullable = false)
//    private Long customer_id;
//
//    @Column(nullable = false)
//    private Long product_id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int total_price;

    @Column(nullable = false)
    private byte status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Builder
    public Reservation(int quantity, int total_price, byte status, LocalDateTime expiredAt) {
//        this.customer_id = customer_id;
//        this.product_id = product_id;
        this.quantity = quantity;
        this.total_price = total_price;
        this.status = status;
        this.expiredAt = expiredAt;
    }
}