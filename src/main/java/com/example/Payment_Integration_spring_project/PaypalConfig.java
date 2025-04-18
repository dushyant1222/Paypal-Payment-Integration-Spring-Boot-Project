package com.example.Payment_Integration_spring_project;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paypal.client")
public class PaypalConfig {
	
	private String id;
	private String secret;
	private String mode;
	private String apiBaseurl;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getApiBaseurl() {
		return apiBaseurl;
	}
	public void setApiBaseurl(String apiBaseurl) {
		this.apiBaseurl = apiBaseurl;
	}
	
}
