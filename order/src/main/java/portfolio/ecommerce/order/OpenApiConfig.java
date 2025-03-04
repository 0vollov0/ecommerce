package portfolio.ecommerce.order;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "e-commerce order",
        version = "1.0.0"
    )
)
public class OpenApiConfig {}