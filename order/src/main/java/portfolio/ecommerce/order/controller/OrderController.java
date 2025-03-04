package portfolio.ecommerce.order.controller;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.ecommerce.order.dto.OrderDto;
import portfolio.ecommerce.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public boolean order(@Valid OrderDto dto) throws BadRequestException {
        return orderService.order(dto);
    }
}
