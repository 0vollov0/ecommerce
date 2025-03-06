package portfolio.ecommerce.worker.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import portfolio.ecommerce.worker.service.SseService;

@Controller
@RequiredArgsConstructor
public class Notification {
    private final SseService sseService;

    @GetMapping("/sse")
    public SseEmitter connect(@RequestParam Long customerId) {
        SseEmitter emitter = new SseEmitter();
        sseService.getClients().put(customerId, emitter);
        return emitter;
    }
}
