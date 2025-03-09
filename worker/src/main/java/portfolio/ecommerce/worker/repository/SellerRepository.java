package portfolio.ecommerce.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.order.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Page<Seller> findAllByDeleted(boolean deleted, Pageable pageable);
}
