package com.example.Payment_Integration_spring_project;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;


@Service
@Transactional
public class PaymentService {
	
	@Autowired
	PaymentRepo paymentRepo;
	
	@Autowired
	PaypalConfig paypalConfig;
	
	public List<Payment> displays(){
		List<Payment> list = paymentRepo.findAll();
		return list;
	}
	
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public String gettoken() {
		
		String credentials = paypalConfig.getId() + ":" + paypalConfig.getSecret();
		String encodedcredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Basic " + encodedcredentials);
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<String> request = new HttpEntity<String>("grant_type=client_credentials", httpHeaders);
		
		ResponseEntity<String> response = restTemplate.exchange(paypalConfig.getApiBaseurl() + "/v1/oauth2/token",HttpMethod.POST,request,String.class);
		
		JSONObject jsonObject = new JSONObject(response.getBody());
		System.out.println("PayPal Token Response: " + jsonObject.toString());
		return jsonObject.getString("access_token");
		
	}

}
