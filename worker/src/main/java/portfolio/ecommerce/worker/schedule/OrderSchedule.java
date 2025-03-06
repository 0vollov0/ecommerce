package portfolio.ecommerce.worker.schedule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import portfolio.ecommerce.worker.entity.StockLock;
import portfolio.ecommerce.worker.repository.StockLockRepository;
import portfolio.ecommerce.worker.service.PaymentService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderSchedule {
    private static final Logger log = LogManager.getLogger(OrderSchedule.class);
    @PersistenceContext
    EntityManager em;
    private final StockLockRepository stockLockRepository;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void processExpiredStocks() {
        LocalDateTime now = LocalDateTime.now();
        List<StockLock> stockLockList = em.createQuery("SELECT s FROM StockLock s WHERE s.expiredAt < :now", StockLock.class)
                .setParameter("now", now)
                .getResultList();
        log.info("stockLockList size {}", stockLockList.size());
        for (StockLock stockLock: stockLockList) {
            stockLock.getOrder().getCustomer().setAmount(stockLock.getSalesPrice() + stockLock.getOrder().getCustomer().getAmount());
            stockLock.getProduct().setStock(stockLock.getProduct().getStock() + stockLock.getQuantity());
            em.remove(stockLock);
            log.info("❌ id:{} stock_lock 삭제 완료 ❌", stockLock.getStockLockId());
        }
    }
}

