package portfolio.ecommerce.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.worker.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
