package portfolio.ecommerce.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.worker.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> { }
