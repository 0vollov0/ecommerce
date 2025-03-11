package portfolio.ecommerce.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import portfolio.ecommerce.order.interceptor.OrderInterceptor;

@Configuration
public class OrderConfig implements WebMvcConfigurer {
    private final OrderInterceptor orderInterceptor;

    public OrderConfig(OrderInterceptor orderInterceptor) {
        this.orderInterceptor = orderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(orderInterceptor)
                .addPathPatterns("/orders");
    }
}
