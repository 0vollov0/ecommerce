package portfolio.ecommerce.billing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @GetMapping
    public boolean get() {
        return false;
    }

    @PostMapping
    public boolean payment() {
        return true;
    }
}
