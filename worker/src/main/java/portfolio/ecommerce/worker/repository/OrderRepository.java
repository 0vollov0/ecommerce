package portfolio.ecommerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import portfolio.ecommerce.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> { }
