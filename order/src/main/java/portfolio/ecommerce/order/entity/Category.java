package portfolio.ecommerce.order.entity;

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
    private Long categoryId;

    @Column(length = 45)
    private String name;

    @Builder
    public Category(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
