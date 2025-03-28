package portfolio.ecommerce.worker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long customerId;

    @Column
    private String name;

    @Column
    private int amount;

    @JsonIgnore
    @Column
    private boolean deleted;

    @Builder
    public Customer(Long customerId, String name, int amount, boolean deleted) {
        this.customerId = customerId;
        this.name = name;
        this.amount = amount;
        this.deleted = deleted;
    }
}