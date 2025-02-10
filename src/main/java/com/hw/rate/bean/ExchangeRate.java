package com.hw.rate.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRate {
	private String date;
	private double rate;
}
