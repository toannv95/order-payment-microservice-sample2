package org.toannguyen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class StockApp {
    public static void main(String[] args) {
        SpringApplication.run(StockApp.class, args);
    }
}