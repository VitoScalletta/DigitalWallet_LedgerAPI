package com.microcommerce.digitalwallet_ledgerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DigitalWalletLedgerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalWalletLedgerApiApplication.class, args);
    }

}
