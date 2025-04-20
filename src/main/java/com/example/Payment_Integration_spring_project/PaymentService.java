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
	

	public String create_order(double amount) {
		String accesstoken = gettoken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accesstoken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject amountobj = new JSONObject();
		amountobj.put("currency_code", "USD");
		amountobj.put("value", amount);
		
		JSONObject purchaseunit = new JSONObject();
		purchaseunit.put("amount", amountobj);
		
		JSONObject appcontext = new JSONObject();
		appcontext.put("return_url", "http://localhost:8080/pay/sucess");
		appcontext.put("cancel_url", "http://localhost:8080/pay/cancel");
		
		JSONObject payload = new JSONObject();
		payload.put("intent", "CAPTURE");
		payload.put("purchase_units", Collections.singletonList(purchaseunit));
		payload.put("application_context", appcontext);
		
		HttpEntity<String> entity = new HttpEntity<String>(payload.toString(), headers);
		
		ResponseEntity<String> response = restTemplate.exchange(paypalConfig.getApiBaseurl() +  "/v2/checkout/orders", HttpMethod.POST,entity, String.class);
		
		JSONObject responsejson = new JSONObject(response.getBody());
		System.out.println("PayPal Order Response: " + responsejson.toString());

		for(Object linkObject: responsejson.getJSONArray("links")) {
			
			JSONObject link = (JSONObject) linkObject;
			if(link.getString("rel").equals("approve")) {
				return link.getString("href"); //approival link is heree
				
			}
			System.out.println("Checking link: " + link.toString());

		}
		
		return null;
		
	}
	

	public String captureorder(String oderId) {
		String accesstoken = gettoken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accesstoken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> entity = new HttpEntity<String>(null,headers);
		
		ResponseEntity<String> response = restTemplate.exchange(paypalConfig.getApiBaseurl() + "/v2/checkout/orders/" + oderId + "/capture", HttpMethod.POST, entity, String.class);
		
		return response.getBody();
		
		
	}
	
	
	
	@PostConstruct
	public void debugConfig() {
	    System.out.println("Validated Config:");
	    System.out.println("Client ID: " + paypalConfig.getId());
	    System.out.println("Secret: " + (paypalConfig.getSecret() != null ? "***" : "NULL"));
	    System.out.println("API URL: " + paypalConfig.getApiBaseurl());
	}

}
