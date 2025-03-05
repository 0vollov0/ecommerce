package portfolio.ecommerce.payment.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.payment.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> { }
