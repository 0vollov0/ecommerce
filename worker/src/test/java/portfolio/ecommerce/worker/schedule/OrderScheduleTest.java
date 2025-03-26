package portfolio.ecommerce.worker.schedule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import portfolio.ecommerce.worker.service.StockLockService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderScheduleTest {

    @InjectMocks
    private OrderSchedule orderSchedule;

    @Mock
    private StockLockService stockLockService;

    private static final Logger log = LoggerFactory.getLogger(OrderScheduleTest.class);

    @Test
    void shouldProcessExpiredStocksSuccessfully() {
        when(stockLockService.processExpiredStockLocks()).thenReturn(5);

        orderSchedule.processExpiredStocks();

        verify(stockLockService, times(1)).processExpiredStockLocks();
    }

    @Test
    void shouldProcessExpiredStocksWhenNoExpiredLocksExist() {
        when(stockLockService.processExpiredStockLocks()).thenReturn(0);

        orderSchedule.processExpiredStocks();

        verify(stockLockService, times(1)).processExpiredStockLocks();
    }
}
