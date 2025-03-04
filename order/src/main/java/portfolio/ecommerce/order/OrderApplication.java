package portfolio.ecommerce.order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OrderApplication {
	private static final Logger log = LogManager.getLogger(OrderApplication.class);
	public static void main(String[] args) {
		log.info("OrderApplication is running....");
		SpringApplication.run(OrderApplication.class, args);
	}
}
