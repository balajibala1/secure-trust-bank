package com.bank.credit_card_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.com.bank.credit_card_service,com.bank.authorization"})
@EnableConfigurationProperties
public class CreditCardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditCardServiceApplication.class, args);
	}

}