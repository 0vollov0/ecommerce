package portfolio.ecommerce.order.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import portfolio.ecommerce.order.entity.StockLock;

import java.util.List;

public interface StockLockRepository extends CrudRepository<StockLock, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM StockLock s WHERE s.order.orderId = :orderId")
    void deleteByOrderId(Long orderId);
}
