package portfolio.ecommerce.billing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BillingApplication {
	private static final Logger log = LogManager.getLogger();
	public static void main(String[] args) {
		log.info("billing application is running....");
		SpringApplication.run(BillingApplication.class, args);
	}
}
