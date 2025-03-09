package portfolio.ecommerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import portfolio.ecommerce.payment.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> { }
