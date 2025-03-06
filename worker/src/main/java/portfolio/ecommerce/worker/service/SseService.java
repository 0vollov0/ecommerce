package portfolio.ecommerce.worker.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Service
public class SseService {
    private final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();
}
