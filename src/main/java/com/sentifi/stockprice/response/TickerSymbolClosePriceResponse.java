package com.sentifi.stockprice.response;

import java.util.ArrayList;
import java.util.List;

import com.sentifi.stockprice.vo.TickerSymbolClosePrice;

public class TickerSymbolClosePriceResponse {
	
	private List<TickerSymbolClosePrice> prices = new ArrayList<TickerSymbolClosePrice>();
	
	public TickerSymbolClosePriceResponse() {
		super();
	}
	
	public TickerSymbolClosePriceResponse(List<TickerSymbolClosePrice> prices) {
		super();
		this.prices = prices;
	}

	public List<TickerSymbolClosePrice> getPrices() {
		return prices;
	}

	public void setPrices(List<TickerSymbolClosePrice> prices) {
		this.prices = prices;
	}
	
}
