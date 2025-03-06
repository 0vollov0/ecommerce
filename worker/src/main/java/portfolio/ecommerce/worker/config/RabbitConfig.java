package portfolio.ecommerce.worker.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String PAYMENT_REQUEST_QUEUE = "worker.request";
    public static final String PAYMENT_RESULT_QUEUE = "worker.result";

    @Bean
    public Queue workerRequestQueue() {
        return new Queue(PAYMENT_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue workerResultQueue() {
        return new Queue(PAYMENT_RESULT_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}