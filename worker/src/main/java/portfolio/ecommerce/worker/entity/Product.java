package portfolio.ecommerce.worker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Column
    private String name;

    @PositiveOrZero
    @Column(nullable = false)
    private int salesPrice;

    @PositiveOrZero
    @Column(nullable = false)
    private int stock;

    @OneToMany(mappedBy = "product")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<StockLock> stockLocks = new LinkedHashSet<>();

    @Builder
    public Product(String name, int salesPrice, int stock, Category category, Seller seller) {
        this.name = name;
        this.salesPrice = salesPrice;
        this.stock = stock;
        this.category = category;
        this.seller = seller;
    }
}
