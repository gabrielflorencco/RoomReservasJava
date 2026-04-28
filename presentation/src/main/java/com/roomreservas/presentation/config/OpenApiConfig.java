package com.roomreservas.presentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI roomReservasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RoomReservas API")
                        .description("""
                                API para gerenciamento de reservas de salas.
                                
                                **Regras de negócio:**
                                - Usuário pode ter no máximo **3 reservas ativas** (PENDENTE ou CONFIRMADA) simultâneas
                                - Cancelamento permitido somente com **24h de antecedência**
                                - Desativar uma sala **cancela automaticamente** todas as reservas futuras ativas
                                - Valor total calculado proporcionalmente ao valor da diária (24h)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RoomReservas")
                                .email("contato@roomreservas.com"))
                        .license(new License().name("MIT"))
                );
    }
}
