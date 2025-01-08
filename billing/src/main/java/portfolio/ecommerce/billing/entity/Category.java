package portfolio.ecommerce.billing.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    @Column(length = 45)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Builder
    public Category(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
