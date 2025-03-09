package portfolio.ecommerce.worker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "e-commerce worker",
        version = "1.0.0"
    ),
    servers = @Server(url = "/")
)
public class OpenApiConfig {}