package portfolio.ecommerce.main.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.main.entity.Seller;

public interface SellerRepository extends CrudRepository<Seller, Long> { }
