package portfolio.ecommerce.payment.repository;

import org.springframework.data.repository.CrudRepository;
import portfolio.ecommerce.payment.entity.Reservation;

public interface ReservationRepository extends CrudRepository<Reservation, Long> { }
