package com.sentifi.stockprice.vo;

import java.util.ArrayList;
import java.util.List;


public class TickerSymbolClosePrice {
	private String ticker;
	private List<ClosePrice> closeDates  = new ArrayList<ClosePrice>();
	
	public TickerSymbolClosePrice() {
		super();
	}

	public TickerSymbolClosePrice(String ticker, List<ClosePrice> closeDates) {
		super();
		this.ticker = ticker;
		this.closeDates = closeDates;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public List<ClosePrice> getCloseDates() {
		return closeDates;
	}

	public void setCloseDates(List<ClosePrice> closeDates) {
		this.closeDates = closeDates;
	}
	
}
