package com.sentifi.stockprice.vo;

public class ClosePrice {
	private String date;
	private Double close;
	
	public ClosePrice() {
		super();
	}
	
	public ClosePrice(String date, Double close) {
		super();
		this.date = date;
		this.close = close;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double  getClose() {
		return close;
	}

	public void setClose(Double  close) {
		this.close = close;
	}
	
	
	
}
