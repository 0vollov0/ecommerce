package portfolio.ecommerce.order.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.order.dto.OrderDto;
import portfolio.ecommerce.order.entity.Customer;
import portfolio.ecommerce.order.entity.Order;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.entity.StockLock;
import portfolio.ecommerce.order.repository.CustomerRepository;
import portfolio.ecommerce.order.repository.OrderRepository;
import portfolio.ecommerce.order.repository.ProductRepository;
import portfolio.ecommerce.order.repository.StockLockRepository;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private StockLockRepository stockLockRepository;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Autowired
    public void setStockLockRepository(StockLockRepository stockLockRepository) {
        this.stockLockRepository = stockLockRepository;
    }

    @Transactional()
    public boolean order(OrderDto dto) {
        Customer customer = this.customerRepository.findById(dto.getCustomer_id()).orElseThrow(EntityNotFoundException::new);
        Product product = this.productRepository.findById(dto.getProduct_id()).orElseThrow(EntityNotFoundException::new);

        System.out.print(customer);
        System.out.print(product);
        System.out.print(product.getPrice()*dto.getQuantity());
        if(product.getStock() < dto.getQuantity()) return false;

        Order newOrder = Order.builder()
                .customer(customer)
                .seller(product.getSeller())
                .product(product)
                .quantity(dto.getQuantity())
                .total_price(product.getPrice()*dto.getQuantity())
                .build();

        System.out.print(newOrder);

        orderRepository.save(newOrder);
        product.setStock(product.getStock() - dto.getQuantity());
        productRepository.save(product);
        StockLock stockLock = StockLock.builder()
                .order(newOrder)
                .product(product)
                .quantity(dto.getQuantity())
                .expiredAt(LocalDateTime.now())
                .build();
        stockLockRepository.save(stockLock);
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
