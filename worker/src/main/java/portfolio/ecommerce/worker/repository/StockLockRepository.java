package portfolio.ecommerce.worker.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.worker.entity.StockLock;

public interface StockLockRepository extends CrudRepository<StockLock, Long> { }
