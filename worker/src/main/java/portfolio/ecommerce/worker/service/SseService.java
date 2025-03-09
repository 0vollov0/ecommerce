package portfolio.ecommerce.worker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import portfolio.ecommerce.worker.repository.CustomerRepository;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class SseService {
    private final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();

    private final CustomerRepository customerRepository;

    public ResponseEntity<String> connect(Long customerId) {
        System.out.println("Connecting to customer " + customerId);
        if (clients.get(customerId) != null) return ResponseEntity.status(HttpStatus.CONFLICT).body("Already connected");
        if (!customerRepository.existsById(customerId)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer not found");
        SseEmitter emitter = new SseEmitter(600_000L);
        emitter.onCompletion(() -> clients.remove(customerId));
        emitter.onTimeout(() -> clients.remove(customerId));
        emitter.onError((e) -> clients.remove(customerId));
        clients.put(customerId, emitter);

        try {
            emitter.send(SseEmitter.event().name("connect").data("Connected to SSE"));
        } catch (IOException e) {
            emitter.complete();
        }

        return ResponseEntity.ok("Connected to SSE");
    }

    public SseEmitter getEmitter(Long customerId) {
        return clients.get(customerId);
    }
}
