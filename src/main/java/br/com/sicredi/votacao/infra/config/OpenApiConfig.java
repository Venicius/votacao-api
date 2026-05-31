package br.com.sicredi.votacao.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Votação em Pautas - Sicredi")
                        .version("v1.0.0")
                        .description("API responsável pelo gerenciamento de pautas e sessões de votação, construída com Arquitetura Hexagonal e Server-Driven UI (SDUI).")
                        .contact(new Contact()
                                .name("Venicius Alves de Souza")
                                .email("venicius.alves@gmail.com")));
    }
}
