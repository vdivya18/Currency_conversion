package com.service3.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {


	@Autowired
	private CurrencyExhangeProxy proxy;
	
	//using the rest template 
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,@PathVariable String to ,@PathVariable BigDecimal quantity) {
		
		HashMap<String,String> urivariables =new HashMap<>();
		urivariables.put("from",from);
		urivariables.put("to", to);
		
	   ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",CurrencyConversion.class ,urivariables);
		
	   CurrencyConversion conversion= responseEntity.getBody();
		//return new CurrencyConversion(1000L,from,to,quantity,BigDecimal.ONE,BigDecimal.ONE,"8880");
	   return new CurrencyConversion(conversion.getId(),from,to,quantity,conversion.getConversionMultiple(),quantity.multiply(conversion.getConversionMultiple()),conversion.getEnvironment());
	}
	
	
	//using the feign client
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionWithfeign(@PathVariable String from,@PathVariable String to ,@PathVariable BigDecimal quantity) {
		

		CurrencyConversion conversion = proxy.retrieveValue(from, to);
	 
	   return new CurrencyConversion(conversion.getId(),from,to,quantity,conversion.getConversionMultiple(),quantity.multiply(conversion.getConversionMultiple()),conversion.getEnvironment());
	}
	
	
	
}
