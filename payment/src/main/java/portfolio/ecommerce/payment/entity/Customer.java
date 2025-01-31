package portfolio.ecommerce.payment.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customer_id;

    @Column
    private String name;

    @Column
    private int amount;

    @Builder
    public Customer(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}