package portfolio.ecommerce.worker.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.worker.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> { }
