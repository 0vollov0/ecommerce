package portfolio.ecommerce.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Product extends BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    // ✅ category_id 값을 직접 저장하여 추가적인 SELECT 발생 방지
    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ LAZY 로딩 설정 (JOIN 방지)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    // ✅ seller_id 값을 직접 저장하여 추가적인 SELECT 발생 방지
    @Column(name = "seller_id", insertable = false, updatable = false)
    private Long sellerId;

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ LAZY 로딩 설정 (JOIN 방지)
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

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Order> orders = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<StockLock> stockLocks = new LinkedHashSet<>();

    @JsonIgnore
    @Column
    private boolean deleted;

    @Builder
    public Product(Long productId, String name, int salesPrice, int stock, Long categoryId, Long sellerId, boolean deleted) {
        this.productId = productId;
        this.name = name;
        this.salesPrice = salesPrice;
        this.stock = stock;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.deleted = deleted;

        // 연관 엔티티는 null로 설정 (Lazy 로딩 방지)
        this.category = null;
        this.seller = null;
    }

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException("Stock is not enough to process the order.");
        }
        this.stock -= quantity;
    }

    @PrePersist
    @PreUpdate
    private void syncIds() {
        if (category != null) {
            this.categoryId = category.getCategoryId();
        }
        if (seller != null) {
            this.sellerId = seller.getSellerId();
        }
    }
}
