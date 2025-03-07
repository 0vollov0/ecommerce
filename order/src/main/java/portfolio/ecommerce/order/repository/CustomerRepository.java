package portfolio.ecommerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.order.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> { }
