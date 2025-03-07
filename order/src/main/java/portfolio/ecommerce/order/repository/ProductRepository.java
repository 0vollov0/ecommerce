package portfolio.ecommerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.order.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> { }
