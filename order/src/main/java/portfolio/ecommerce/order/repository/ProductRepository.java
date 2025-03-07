package portfolio.ecommerce.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.order.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByDeleted(boolean deleted, Pageable pageable);
}
