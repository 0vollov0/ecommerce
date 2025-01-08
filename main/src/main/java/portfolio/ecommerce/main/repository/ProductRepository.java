package portfolio.ecommerce.main.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.main.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> { }
