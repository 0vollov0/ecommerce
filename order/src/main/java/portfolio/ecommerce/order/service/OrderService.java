package portfolio.ecommerce.order.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.order.dto.OrderDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.entity.Customer;
import portfolio.ecommerce.order.entity.Order;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.entity.StockLock;
import portfolio.ecommerce.order.repository.CustomerRepository;
import portfolio.ecommerce.order.repository.OrderRepository;
import portfolio.ecommerce.order.repository.ProductRepository;
import portfolio.ecommerce.order.repository.StockLockRepository;
import portfolio.ecommerce.order.response.OrderResponse;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StockLockRepository stockLockRepository;
    private final PaymentRequestSender paymentRequestSender;

    private final RedisService redisService;
    private final UtilService utilService;

    @Transactional()
    public OrderResponse order(OrderDto dto) {
        String uniqueKey = utilService.generateHash(String.valueOf(dto.getCustomer_id() + dto.getProduct_id() + dto.getQuantity()));
        Boolean acquired = redisService.lockData(uniqueKey, "processed", 1);
        if (Boolean.FALSE.equals(acquired)) {
            return OrderResponse.builder().status(HttpStatus.CONFLICT).message("Conflict order").build();
        }

        try {
            Product product = this.productRepository.findByIdForUpdate(dto.getProduct_id()).orElseThrow(EntityNotFoundException::new);
            if (product.getStock() < dto.getQuantity()) {
                return OrderResponse.builder().status(HttpStatus.BAD_REQUEST).message("Not enough stock to order").build();
            }

            Customer customer = this.customerRepository.findById(dto.getCustomer_id()).orElseThrow(EntityNotFoundException::new);
            if (product.getSalesPrice() > customer.getAmount()) {
                return OrderResponse.builder().status(HttpStatus.BAD_REQUEST).message("Not enough amount to order").build();
            }

            product.decreaseStock(dto.getQuantity());
            customer.decreaseAmount(product.getSalesPrice());

            Order newOrder = Order.builder()
                    .customer(customer)
                    .seller(product.getSeller())
                    .product(product)
                    .quantity(dto.getQuantity())
                    .salesPrice(product.getSalesPrice())
                    .build();

            orderRepository.save(newOrder);
            productRepository.save(product);
            customerRepository.save(customer);

            StockLock stockLock = StockLock.builder()
                    .order(newOrder)
                    .product(product)
                    .salesPrice(product.getSalesPrice())
                    .quantity(dto.getQuantity())
                    .expiredAt(LocalDateTime.now().plusMinutes(3))
                    .build();

            stockLockRepository.save(stockLock);

            paymentRequestSender.sendPaymentRequest(stockLock.toPaymentRequestDto());

            return new OrderResponse(HttpStatus.CREATED, newOrder.getOrderId(), "Your order has been proceed");
        } catch (Exception e) {
            redisService.deleteData(uniqueKey); // 실패 시 Lock 해제
            throw new RuntimeException("Order processing failed", e);
        }
    }

    public Page<Order> find(RequestPagingDto dto) {
        return orderRepository.findAll(PageRequest.of(dto.getPage(), dto.getPageSize(), Sort.by("createdAt").descending()));
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
}
