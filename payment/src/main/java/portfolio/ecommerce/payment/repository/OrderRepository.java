package portfolio.ecommerce.payment.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.order.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Long> { }
