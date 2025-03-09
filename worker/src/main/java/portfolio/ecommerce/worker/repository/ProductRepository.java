package portfolio.ecommerce.worker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.worker.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByDeleted(boolean deleted, Pageable pageable);
}
