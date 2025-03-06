package portfolio.ecommerce.worker.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.worker.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> { }
