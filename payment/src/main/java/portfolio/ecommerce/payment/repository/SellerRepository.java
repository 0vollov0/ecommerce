package portfolio.ecommerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.payment.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> { }
