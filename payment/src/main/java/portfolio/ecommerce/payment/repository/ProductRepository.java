package portfolio.ecommerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.payment.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> { }
