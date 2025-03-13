package portfolio.ecommerce.payment.integration;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import portfolio.ecommerce.payment.config.RabbitConfig;
import portfolio.ecommerce.payment.dto.PaymentResultDto;
import portfolio.ecommerce.payment.dto.RequestPaymentDto;
import portfolio.ecommerce.payment.entity.*;
import portfolio.ecommerce.payment.repository.*;
import portfolio.ecommerce.payment.service.PaymentService;
import portfolio.ecommerce.payment.service.RedisService;
import portfolio.ecommerce.payment.service.UtilService;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PaymentServiceIntegrationTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private StockLockRepository stockLockRepository;

    private Product product;
    private Seller seller;
    private Customer customer;
    private Category category;
    private Order order;
    private StockLock stockLock;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UtilService utilService;

    private static final Long existingId = 99999L;

    @BeforeEach
    void setUp() {
        customer = customerRepository.save(Customer.builder().name("1").amount(100000).build());
        category = categoryRepository.save(Category.builder().name("1").build());
        seller = sellerRepository.save(Seller.builder().name("1").build());
        product = productRepository.save(Product.builder().category(category).seller(seller).name("1").salesPrice(10).stock(1000).build());
        order = orderRepository.save(Order.builder().seller(seller).salesPrice(product.getSalesPrice()).customer(customer).product(product).quantity(1).build());
        LocalDateTime oneMinutesLater = LocalDateTime.now().plusMinutes(1);
        stockLock = stockLockRepository.save(StockLock.builder().order(order).salesPrice(order.getSalesPrice()).quantity(order.getQuantity()).product(product).expiredAt(oneMinutesLater).build());
    }

    @AfterEach
    void cleanUp() {
        stockLockRepository.deleteById(stockLock.getStockLockId());
        orderRepository.deleteById(order.getOrderId());
        productRepository.deleteById(product.getProductId());
        customerRepository.deleteById(customer.getCustomerId());
        categoryRepository.deleteById(category.getCategoryId());
        sellerRepository.deleteById(seller.getSellerId());
    }

    @Test
    void processPaymentNotExpired() throws Exception {
        String uniqueKey = utilService.generateHash(String.valueOf(stockLock.getOrder().getCustomer().getCustomerId() + stockLock.getProduct().getProductId() + stockLock.getQuantity()));
        RequestPaymentDto requestPaymentDto = stockLock.toPaymentRequestDto();

        paymentService.processPayment(requestPaymentDto);

        order = orderRepository.findById(order.getOrderId()).orElseThrow(EntityNotFoundException::new);

        assertThat(order.getState()).isEqualTo(1);
        assertThat(stockLockRepository.findById(stockLock.getStockLockId())).isEmpty();
        PaymentResultDto paymentResultDto = (PaymentResultDto) rabbitTemplate.receiveAndConvert(RabbitConfig.PAYMENT_RESULT_QUEUE);
        PaymentResultDto expectedResultDto = new PaymentResultDto(true, order.getOrderId());
        assertThat(paymentResultDto).isInstanceOf(PaymentResultDto.class);
        assertThat(paymentResultDto)
                .extracting(PaymentResultDto::getOrderId, PaymentResultDto::isSucceed)
                .containsExactly(expectedResultDto.getOrderId(), expectedResultDto.isSucceed());
        assertThat(redisService.getData(uniqueKey)).isNull();
    }

    @Test
    void processPaymentExpired() throws Exception {
        String uniqueKey = utilService.generateHash(String.valueOf(stockLock.getOrder().getCustomer().getCustomerId() + stockLock.getProduct().getProductId() + stockLock.getQuantity()));
        stockLock.setExpiredAt(LocalDateTime.now().minusMinutes(1));
        stockLockRepository.save(stockLock);
        RequestPaymentDto requestPaymentDto = stockLock.toPaymentRequestDto();

        paymentService.processPayment(requestPaymentDto);

        order = orderRepository.findById(order.getOrderId()).orElseThrow(EntityNotFoundException::new);

        assertThat(order.getState()).isEqualTo(2);
        assertThat(stockLockRepository.findById(stockLock.getStockLockId())).isEmpty();
        PaymentResultDto paymentResultDto = (PaymentResultDto) rabbitTemplate.receiveAndConvert(RabbitConfig.PAYMENT_RESULT_QUEUE);
        PaymentResultDto expectedResultDto = new PaymentResultDto(false, order.getOrderId());
        assertThat(paymentResultDto).isInstanceOf(PaymentResultDto.class);
        assertThat(paymentResultDto)
                .extracting(PaymentResultDto::getOrderId, PaymentResultDto::isSucceed)
                .containsExactly(expectedResultDto.getOrderId(), expectedResultDto.isSucceed());
        assertThat(redisService.getData(uniqueKey)).isNull();
    }
}
