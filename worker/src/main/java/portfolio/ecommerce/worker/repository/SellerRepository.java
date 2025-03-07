package portfolio.ecommerce.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.worker.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> { }
