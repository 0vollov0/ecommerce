package portfolio.ecommerce.payment.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.payment.entity.Customer;
import portfolio.ecommerce.payment.entity.Product;
import portfolio.ecommerce.payment.entity.StockLock;
import portfolio.ecommerce.payment.repository.CustomerRepository;
import portfolio.ecommerce.payment.repository.ProductRepository;
import portfolio.ecommerce.payment.repository.StockLockRepository;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {
    private final StockLockRepository stockLockRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public void processExpiredStock(Long stockLockId) {
        StockLock stockLock = stockLockRepository.findById(stockLockId).orElseThrow(EntityNotFoundException::new);
        Product product = productRepository.findById(stockLock.getProduct().getProductId()).orElseThrow(EntityNotFoundException::new);
        product.setStock(product.getStock() + stockLock.getQuantity());
        productRepository.save(product);
        Customer customer = customerRepository.findById(stockLock.getOrder().getCustomer().getCustomerId()).orElseThrow(EntityNotFoundException::new);
        customer.setAmount(customer.getAmount() + stockLock.getSalesPrice());
        customerRepository.save(customer);
    }
}
