package com.roomreservas.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.roomreservas")
@EntityScan(basePackages = "com.roomreservas.domain.entity")
@EnableJpaRepositories(basePackages = "com.roomreservas.infrastructure.persistence.repository")
public class RoomReservasApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomReservasApplication.class, args);
    }
}
