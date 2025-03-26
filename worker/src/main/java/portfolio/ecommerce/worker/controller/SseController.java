package portfolio.ecommerce.worker.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommerce.worker.response.ApiErrorResponses;
import portfolio.ecommerce.worker.service.SseService;

@RestController()
@RequestMapping("/sse")
@Tag(name = "sse")
public class SseController {
    private final SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    @ApiErrorResponses
    @GetMapping
    public ResponseEntity<String> connect(@RequestParam Long customerId) {
        return this.sseService.connect(customerId);
    }
}
