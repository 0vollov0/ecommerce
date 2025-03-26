package portfolio.ecommerce.worker.schedule;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import portfolio.ecommerce.worker.service.StockLockService;

@Component
@RequiredArgsConstructor
public class OrderSchedule {
    private static final Logger log = LogManager.getLogger(OrderSchedule.class);

    private final StockLockService stockLockService;

    @Scheduled(fixedRate = 5000)
    public void processExpiredStocks() {
        int processedCount = stockLockService.processExpiredStockLocks();
        log.info("✅ 처리 완료: {}개의 stock lock 삭제", processedCount);
    }
}

