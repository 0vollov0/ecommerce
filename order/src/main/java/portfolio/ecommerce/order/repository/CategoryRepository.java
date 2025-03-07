package portfolio.ecommerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.order.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
