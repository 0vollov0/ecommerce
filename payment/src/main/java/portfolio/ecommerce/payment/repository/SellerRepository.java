package portfolio.ecommerce.payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.ecommerce.payment.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Page<Seller> findAllByDeleted(boolean deleted, Pageable pageable);
    boolean existsBySellerIdAndDeletedTrue(Long customerId);
}
