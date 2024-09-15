package com.bank.transaction_online_banking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@EnableTransactionManagement
@EnableAsync
@ComponentScan(basePackages = {"com.bank.transaction_online_banking_service", "com.bank.authorization"})
public class TransactionOnlineBankingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionOnlineBankingServiceApplication.class, args);
	}


}
