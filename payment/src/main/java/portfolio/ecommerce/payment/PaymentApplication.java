package portfolio.ecommerce.payment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PaymentApplication {
	private static final Logger log = LogManager.getLogger(PaymentApplication.class);
	public static void main(String[] args) {
		log.info("PaymentApplication is running....");
		SpringApplication.run(PaymentApplication.class, args);
	}
}
