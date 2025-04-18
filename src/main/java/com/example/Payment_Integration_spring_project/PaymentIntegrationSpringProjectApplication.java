package com.example.Payment_Integration_spring_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PaypalConfig.class)
public class PaymentIntegrationSpringProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentIntegrationSpringProjectApplication.class, args);
	}

}
