package com.bank.account_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bank.account_service","com.bank.authorization"})
@EnableDiscoveryClient
public class AccountService {
	public static void main(String[] args) {
		SpringApplication.run(AccountService.class, args);
	}

}
