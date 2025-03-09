package portfolio.ecommerce.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.order.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAllByDeleted(boolean deleted, Pageable pageable);
}
