package portfolio.ecommerce.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portfolio.ecommerce.worker.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.orderId = :orderId")
    List<Order> findByOrderId(@Param("orderId") Long orderId);
}
