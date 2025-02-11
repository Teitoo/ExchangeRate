package com.hw.rate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hw.rate.bean.Currency;
import com.hw.rate.bean.ExchangeRate;
import com.hw.rate.service.ExchangeRateService;

@Controller
@RequestMapping("/exchange-rate")
public class ExchangeRateController {
	@Autowired
	private ExchangeRateService exchangeRateService;
	
	@GetMapping("")
	public String showExchange(@RequestParam(defaultValue = "USD") String currency, Model model) {
		Currency selectedCurr = Currency.valueOf(currency);
		List<ExchangeRate> es = exchangeRateService.fetchExchangeRates(selectedCurr);
		
		List<String> dates = new ArrayList<>();
		List<Double> rates = new ArrayList<>();
		es.forEach(e -> {
			dates.add(e.getDate());
			rates.add(e.getRate());
		});
		model.addAttribute("selectedCurr", selectedCurr);
		model.addAttribute("currencies", Currency.values());
		model.addAttribute("dates", dates);
		model.addAttribute("rates", rates);
		
		return "exchange-rate";
	}
	
}
