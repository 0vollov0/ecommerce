package portfolio.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seller_id;

    @Column
    private String name;

    @Builder
    public Seller(String name) {
        this.name = name;
    }
}