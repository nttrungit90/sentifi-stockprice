package com.sentifi.stockprice.response;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class TickerSymbol200DMAClosePriceListResponse {
	
	@ApiModelProperty(notes = "List of 200 days moving average close price of ticker symbols")
	private List<TickerSymbol200DMAClosePriceResponse> tickerSymbols200dma = new ArrayList<TickerSymbol200DMAClosePriceResponse>();
	
	@ApiModelProperty(notes = "List of invalid ticker symbols")
	private List<ErrorResponse> invalidTickers  = new ArrayList<ErrorResponse>();
	
	public TickerSymbol200DMAClosePriceListResponse() {
		super();
	}
	
	
	public TickerSymbol200DMAClosePriceListResponse(List<TickerSymbol200DMAClosePriceResponse> tickerSymbols200dma,
			List<ErrorResponse> invalidTickers) {
		super();
		this.tickerSymbols200dma = tickerSymbols200dma;
		this.invalidTickers = invalidTickers;
	}


	public List<TickerSymbol200DMAClosePriceResponse> getTickerSymbols200dma() {
		return tickerSymbols200dma;
	}
	public void setTickerSymbols200dma(List<TickerSymbol200DMAClosePriceResponse> tickerSymbols200dma) {
		this.tickerSymbols200dma = tickerSymbols200dma;
	}
	public List<ErrorResponse> getInvalidTickers() {
		return invalidTickers;
	}
	public void setInvalidTickers(List<ErrorResponse> invalidTickers) {
		this.invalidTickers = invalidTickers;
	}
}
