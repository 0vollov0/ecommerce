package portfolio.ecommerce.payment.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.order.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> { }
