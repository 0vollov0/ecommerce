package portfolio.ecommerce.order.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.order.dto.OrderDto;
import portfolio.ecommerce.order.entity.Customer;
import portfolio.ecommerce.order.entity.Order;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.repository.CustomerRepository;
import portfolio.ecommerce.order.repository.OrderRepository;
import portfolio.ecommerce.order.repository.ProductRepository;

@Service
public class OrderService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional()
    public boolean order(OrderDto dto) {
        Customer customer = this.customerRepository.findById(dto.getCustomer_id()).orElseThrow(EntityNotFoundException::new);
        Product product = this.productRepository.findById(dto.getProduct_id()).orElseThrow(EntityNotFoundException::new);

        if(product.getStock() < dto.getQuantity()) return false;

        Order newOrder = Order.builder().product(product).seller(product.getSeller()).customer(customer).total_price(product.getPrice()*dto.getQuantity()).build();
        orderRepository.save(newOrder);
        product.setStock(product.getStock() - dto.getQuantity());
        productRepository.save(product);
        return true;
    }

//
//    @Transactional()
//    public boolean order(OrderDTO dto) throws BadRequestException {
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
