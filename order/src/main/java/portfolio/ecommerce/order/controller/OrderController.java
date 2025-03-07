package portfolio.ecommerce.order.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommerce.order.dto.OrderDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.entity.Order;
import portfolio.ecommerce.order.response.ApiErrorResponses;
import portfolio.ecommerce.order.response.OrderResponse;
import portfolio.ecommerce.order.service.OrderService;

import java.util.Optional;

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
    public OrderResponse order(@Valid OrderDto dto) throws BadRequestException {
        return orderService.order(dto);
    }

    @ApiErrorResponses
    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(@ModelAttribute RequestPagingDto dto) {
        return ResponseEntity.ok().body(this.orderService.find(dto));
    }

    @ApiErrorResponses
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Order>> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.orderService.findById(id));
    }
}
