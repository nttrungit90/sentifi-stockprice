package com.sentifi.stockprice.response;

import java.util.ArrayList;
import java.util.List;

public class TickerSymbol200DMAClosePriceListResponse {
	private List<TickerSymbol200DMAClosePriceResponse> tickerSymbols200dma = new ArrayList<TickerSymbol200DMAClosePriceResponse>();
	private List<ErrorResponse> invalidTickers  = new ArrayList<ErrorResponse>();
	private List<ErrorResponse> noDataFoundTickers  = new ArrayList<ErrorResponse>();
	
	public TickerSymbol200DMAClosePriceListResponse() {
		super();
	}
	
	
	public TickerSymbol200DMAClosePriceListResponse(List<TickerSymbol200DMAClosePriceResponse> tickerSymbols200dma,
			List<ErrorResponse> invalidTickers, List<ErrorResponse> noDataFoundTickers) {
		super();
		this.tickerSymbols200dma = tickerSymbols200dma;
		this.invalidTickers = invalidTickers;
		this.noDataFoundTickers = noDataFoundTickers;
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

	public List<ErrorResponse> getNoDataFoundTickers() {
		return noDataFoundTickers;
	}

	public void setNoDataFoundTickers(List<ErrorResponse> noDataFoundTickers) {
		this.noDataFoundTickers = noDataFoundTickers;
	}
	
}
