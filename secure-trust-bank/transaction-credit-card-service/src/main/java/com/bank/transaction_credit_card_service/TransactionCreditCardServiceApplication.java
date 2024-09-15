package com.bank.transaction_credit_card_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.bank.transaction_credit_card_service","com.bank.authorization"})
public class TransactionCreditCardServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TransactionCreditCardServiceApplication.class, args);
	}
}
