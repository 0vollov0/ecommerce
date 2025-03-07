package portfolio.ecommerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.order.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> { }
