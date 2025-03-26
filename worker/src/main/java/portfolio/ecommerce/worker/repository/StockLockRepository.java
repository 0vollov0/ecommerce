package portfolio.ecommerce.worker.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.worker.entity.StockLock;

import java.time.LocalDateTime;
import java.util.List;

public interface StockLockRepository extends CrudRepository<StockLock, Long> {
    @Query("SELECT s FROM StockLock s WHERE s.expiredAt < :now")
    List<StockLock> findAllExpired(LocalDateTime now);
}
