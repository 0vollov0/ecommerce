package portfolio.ecommerce.payment;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "e-commerce payment",
        version = "1.0.0"
    )
)
public class OpenApiConfig {}