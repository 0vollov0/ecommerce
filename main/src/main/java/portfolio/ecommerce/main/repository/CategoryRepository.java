package portfolio.ecommerce.main.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.main.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> { }
