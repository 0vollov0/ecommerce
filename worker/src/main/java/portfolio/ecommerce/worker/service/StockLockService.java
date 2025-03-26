package portfolio.ecommerce.worker.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.worker.entity.StockLock;
import portfolio.ecommerce.worker.repository.StockLockRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockLockService {
    private static final Logger log = LogManager.getLogger(StockLockService.class);

    private final StockLockRepository stockLockRepository;
    private final EntityManager entityManager;

    @Transactional
    public int processExpiredStockLocks() {
        LocalDateTime now = LocalDateTime.now();
        List<StockLock> stockLockList = stockLockRepository.findAllExpired(now);
        log.info("🔍 만료된 StockLock 개수: {}", stockLockList.size());

        for (StockLock stockLock: stockLockList) {
            stockLock.getOrder().getCustomer().setAmount(
                    stockLock.getSalesPrice() + stockLock.getOrder().getCustomer().getAmount()
            );
            stockLock.getProduct().setStock(
                    stockLock.getProduct().getStock() + stockLock.getQuantity()
            );
            entityManager.remove(stockLock);
            log.info("❌ id:{} stock_lock 삭제 완료 ❌", stockLock.getStockLockId());
        }
        return stockLockList.size();
    }
}
