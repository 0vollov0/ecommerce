package portfolio.ecommerce.main.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.main.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> { }
