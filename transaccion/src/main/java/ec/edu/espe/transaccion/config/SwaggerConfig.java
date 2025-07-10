package ec.edu.espe.transaccion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Transacciones de Turno - Banquito")
                        .version("1.0")
                        .description("API para procesar transacciones de los turnos de cajeros")
                        .contact(new Contact()
                                .name("Banquito")
                                .email("soporte@banquito.com")));
    }
} 