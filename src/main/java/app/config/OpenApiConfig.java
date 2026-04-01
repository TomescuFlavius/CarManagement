package app.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI oneModelSimpleOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("One Model Simple API")
                        .description("Beginner-friendly API for managing cars")
                        .version("v1")
                        .contact(new Contact().name("One Model Simple Team"))
                        .license(new License().name("Apache 2.0")));
    }
}
