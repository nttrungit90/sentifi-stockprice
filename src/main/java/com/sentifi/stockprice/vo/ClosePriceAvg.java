package com.sentifi.stockprice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class ClosePriceAvg {
	
	@ApiModelProperty(notes = "Ticker symbol")
	@JsonProperty("Ticker")
	private String ticker;
	
	@ApiModelProperty(notes = "Average close price")
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
