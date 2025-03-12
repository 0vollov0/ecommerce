package portfolio.ecommerce.payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.payment.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByDeleted(boolean deleted, Pageable pageable);
    boolean existsByProductIdAndDeletedTrue(Long productId);
}
