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
	
	public List<Payment> displays(){
		List<Payment> list = paymentRepo.findAll();
		return list;
	}
	
	
}
