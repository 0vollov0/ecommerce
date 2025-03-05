package portfolio.ecommerce.payment.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.payment.entity.StockLock;

public interface StockLockRepository extends CrudRepository<StockLock, Long> { }
