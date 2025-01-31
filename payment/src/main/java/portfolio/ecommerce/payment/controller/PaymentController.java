package portfolio.ecommerce.payment.controller;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.ecommerce.payment.dto.PaymentDTO;
import portfolio.ecommerce.payment.service.ReservationService;

//@RestController
//@RequestMapping("/payment")
public class PaymentController {
//    @Autowired
//    private ReservationService reservationService;
//
//    @PostMapping
//    public boolean payment(@Valid PaymentDTO dto) throws BadRequestException {
//        return reservationService.payment(dto);
//    }
}
