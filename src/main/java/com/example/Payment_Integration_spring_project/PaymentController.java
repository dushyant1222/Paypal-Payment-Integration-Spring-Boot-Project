package com.example.Payment_Integration_spring_project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.annotation.PostConstruct;

@Controller
public class PaymentController {
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	PaypalConfig paypalConfig;

	
	@RequestMapping("/show")
	public String display() {
		List<Payment> payments = paymentService.displays();
		System.out.println("Here is the data below");
		for(Payment p: payments) {
			System.out.println(p.getId() + " " + p.getOrder_id()+ " "+p.getAmount()+" "+p.getStatus());
		}
		return "index";
	}
	
	
	
}
