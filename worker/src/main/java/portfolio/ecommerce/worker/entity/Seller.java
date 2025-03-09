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
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @Column
    private String name;

    @JsonIgnore
    @Column
    private boolean deleted;

    @Builder
    public Seller(String name) {
        this.name = name;
    }
}