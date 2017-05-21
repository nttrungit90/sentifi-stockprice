package com.sentifi.stockprice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sentifi.stockprice.vo.ClosePriceAvg;

public class TickerSymbol200DMAClosePriceResponse {
	
	@JsonProperty("200dma")
	private ClosePriceAvg closePrice200DMA;
	
	public TickerSymbol200DMAClosePriceResponse() {
		super();
	}
	
	public TickerSymbol200DMAClosePriceResponse(ClosePriceAvg closePrice200DMA) {
		super();
		this.closePrice200DMA = closePrice200DMA;
	}

	public ClosePriceAvg getClosePrice200DMA() {
		return closePrice200DMA;
	}

	public void setClosePrice200DMA(ClosePriceAvg closePrice200DMA) {
		this.closePrice200DMA = closePrice200DMA;
	}
	
	
}
