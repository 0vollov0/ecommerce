package portfolio.ecommerce.order.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Transactional()
    public OrderResponse order(OrderDto dto) {
        Customer customer = this.customerRepository.findById(dto.getCustomer_id()).orElseThrow(EntityNotFoundException::new);
        Product product = this.productRepository.findById(dto.getProduct_id()).orElseThrow(EntityNotFoundException::new);
        if(product.getStock() < dto.getQuantity()) return new OrderResponse(false, "Not enough stock to order");
        int salesPrice = dto.getQuantity()*product.getSalesPrice();
        if(salesPrice > customer.getAmount()) return new OrderResponse(false, "Not enough amount to order");

        Order newOrder = Order.builder()
                .customer(customer)
                .seller(product.getSeller())
                .product(product)
                .quantity(dto.getQuantity())
                .salesPrice(salesPrice)
                .build();

        orderRepository.save(newOrder);
        product.setStock(product.getStock() - dto.getQuantity());
        productRepository.save(product);
        StockLock stockLock = StockLock.builder()
                .order(newOrder)
                .product(product)
                .salesPrice(salesPrice)
                .quantity(dto.getQuantity())
                .expiredAt(LocalDateTime.now().plusMinutes(3))
                .build();
        customer.setAmount(customer.getAmount() - salesPrice);
        customerRepository.save(customer);
        stockLockRepository.save(stockLock);
        this.paymentRequestSender.sendPaymentRequest(stockLock.toPaymentRequestDto());
        return new OrderResponse(true, "Your order has been proceed");
    }

    public Page<Order> find(RequestPagingDto dto) {
        return orderRepository.findAll(PageRequest.of(dto.getPage(), dto.getPageSize(), Sort.by("createdAt").descending()));
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
}
