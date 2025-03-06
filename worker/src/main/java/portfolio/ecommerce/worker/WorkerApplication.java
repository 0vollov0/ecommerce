package portfolio.ecommerce.worker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class WorkerApplication {
	private static final Logger log = LogManager.getLogger(WorkerApplication.class);
	public static void main(String[] args) {
		log.info("WorkerApplication is running....");
		SpringApplication.run(WorkerApplication.class, args);
	}
}
