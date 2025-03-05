package portfolio.ecommerce.payment.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.payment.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Long> { }
