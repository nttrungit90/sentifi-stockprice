package com.sentifi.stockprice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClosePriceAvg {
	
	@JsonProperty("Ticker")
	private String ticker;
	
	@JsonProperty("Avg")
	private String avg;
	
	public ClosePriceAvg() {
		super();
	}
	
	public ClosePriceAvg(String ticker, String avg) {
		super();
		this.ticker = ticker;
		this.avg = avg;
	}
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public String getAvg() {
		return avg;
	}
	public void setAvg(String avg) {
		this.avg = avg;
	}
	
	
}
