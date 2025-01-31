package portfolio.ecommerce.payment.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.payment.dto.PaymentDTO;
import portfolio.ecommerce.payment.entity.Product;
import portfolio.ecommerce.payment.entity.Reservation;

import java.time.LocalDateTime;

//@Service
public class ReservationService {
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Transactional()
//    public boolean payment(PaymentDTO dto) throws BadRequestException {
//        Product product = productRepository.findById(dto.getProduct_id()).orElseThrow(EntityNotFoundException::new);
//
//        if (product.getQuantity() < dto.getQuantity()) {
//            throw new BadRequestException("");
//        }
//
//        product.setQuantity(product.getQuantity() - dto.getQuantity());
//        productRepository.save(product);
//
//        Reservation reservation = Reservation.builder()
//                .customer_id(dto.getCustomer_id())
//                .product_id(dto.getProduct_id())
//                .total_price(product.getPrice()*dto.getQuantity())
//                .quantity(dto.getQuantity())
//                .expiredAt(LocalDateTime.now().plusMinutes(10))
//                .build();
//
//        reservationRepository.save(reservation);
//
//        return false;
//    }
}
