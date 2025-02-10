package com.hw.rate.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hw.rate.bean.Currency;
import com.hw.rate.bean.ExchangeRate;

@Service
public class ExchangeRateService {
	private static final String POST_URL = "https://www.esunbank.com/api/client/ExchangeRate/ExchangeRateChart";
	private static final String ITEM_ID = "FF9F6986FD574497B5C6CC93B9838C6D";
	
	public List<ExchangeRate> fetchExchangeRates(Currency c) {
		List<ExchangeRate> exchangeRates = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();
		
		LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);
        
        String requestBody = String.format("{\"Currency\":\"%s\","
        		+ "\"CurrencyType\":\"spot\","
        		+ "\"StartDate\":\"%s\","
        		+ "\"EndDate\":\"%s\","
        		+ "\"ItemId\":\"%s\"}", c, startDate, endDate, ITEM_ID);
        
        HttpRequest request = HttpRequest.newBuilder()
        		.uri(URI.create(POST_URL))
        		.header("Content-Type", "application/json")
        		.POST(HttpRequest.BodyPublishers.ofString(requestBody))
        		.build();
        try {
        	HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        	
        	ObjectMapper objectMapper = new ObjectMapper();
        	JsonNode jsonNode = objectMapper.readTree(response.body());
        	JsonNode dataArray = jsonNode.get("Data");
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	
        	if (dataArray != null) {
                JsonNode rateList = dataArray.get(0);

                for (JsonNode rateEntry : rateList) {
                    long timestamp = rateEntry.get(0).asLong();
                    //時間戳轉換
                    String date = sdf.format(new Date(timestamp));
                    double rate = rateEntry.get(1).asDouble();
                    
                    exchangeRates.add(new ExchangeRate(date, rate));
                }
             }
        } catch(Exception e) {
        	e.printStackTrace();
        }
        return exchangeRates;
	}
}
