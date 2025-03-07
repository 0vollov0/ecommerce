package portfolio.ecommerce.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.worker.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> { }
