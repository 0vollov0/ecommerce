package portfolio.ecommerce.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @Column(name = "seller_id", insertable = false, updatable = false)
    private Long sellerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonIgnore
    private Seller seller;

    @Column
    private String name;

    @PositiveOrZero
    @Column(nullable = false)
    private int salesPrice;

    @PositiveOrZero
    @Column(nullable = false)
    private int stock;

//    @JsonIgnore
//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    private Set<Order> orders = new LinkedHashSet<>();
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    private Set<StockLock> stockLocks = new LinkedHashSet<>();

    @JsonIgnore
    @Column
    private boolean deleted;

    @Builder
    public Product(Long productId, String name, int salesPrice, int stock, Long categoryId, Long sellerId, boolean deleted, Category category, Seller seller) {
        this.productId = productId;
        this.name = name;
        this.salesPrice = salesPrice;
        this.stock = stock;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.deleted = deleted;

        this.category = category;
        this.seller = seller;
    }
}
