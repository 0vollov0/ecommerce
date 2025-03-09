package portfolio.ecommerce.worker.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import portfolio.ecommerce.worker.response.ApiErrorResponses;
import portfolio.ecommerce.worker.service.SseService;

@RestController()
@RequestMapping("/sse")
@Tag(name = "sse")
public class SseController {
    private SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    @ApiErrorResponses
    @GetMapping
    public ResponseEntity<String> connect(@RequestParam Long customerId) {
        return this.sseService.connect(customerId);
    }
}
