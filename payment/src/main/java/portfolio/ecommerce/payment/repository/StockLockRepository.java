package portfolio.ecommerce.payment.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.order.entity.StockLock;

public interface StockLockRepository extends CrudRepository<StockLock, Long> { }
