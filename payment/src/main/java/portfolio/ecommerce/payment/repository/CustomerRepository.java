package portfolio.ecommerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.payment.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> { }
