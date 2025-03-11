package portfolio.ecommerce.order.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommerce.order.dto.OrderDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.entity.Order;
import portfolio.ecommerce.order.response.ApiErrorResponses;
import portfolio.ecommerce.order.response.OrderResponse;
import portfolio.ecommerce.order.service.OrderService;

@Validated
@RestController
@RequestMapping("/orders")
@Tag(name = "orders")
@RequiredArgsConstructor
public class OrderController {
    private static final Logger log = LogManager.getLogger(OrderController.class);
    private final OrderService orderService;

    @ApiErrorResponses
    @PostMapping
    public ResponseEntity<OrderResponse> order(@RequestBody @Valid OrderDto dto) throws BadRequestException {
        OrderResponse response = orderService.order(dto);
        if (response.isResult()) return ResponseEntity.status(HttpStatus.CREATED).body(response);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ApiErrorResponses
    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(@ModelAttribute RequestPagingDto dto) {
        return ResponseEntity.ok(this.orderService.find(dto));
    }

    @ApiErrorResponses
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // Optional 처리 개선
    }
}
