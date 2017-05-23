package com.sentifi.stockprice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sentifi.stockprice.vo.ClosePriceAvg;

import io.swagger.annotations.ApiModelProperty;

public class TickerSymbol200DMAClosePriceResponse {
	
	@ApiModelProperty(notes = "200 day moving average close price of a ticker symbol")
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
