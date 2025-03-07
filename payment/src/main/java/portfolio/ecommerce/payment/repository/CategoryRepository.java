package portfolio.ecommerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.payment.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
