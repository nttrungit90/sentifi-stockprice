package com.sentifi.stockprice.business.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sentifi.stockprice.business.service.TickerSymbolService;
import com.sentifi.stockprice.datasource.TickerSymbolDataSource;
import com.sentifi.stockprice.datasource.rest.converter.QuandlTickerSymbol2StockPriceVOConverter;
import com.sentifi.stockprice.datasource.rest.model.QuandlTickerSymbol;
import com.sentifi.stockprice.exception.ErrorCode;
import com.sentifi.stockprice.exception.StockPriceException;
import com.sentifi.stockprice.response.ErrorResponse;
import com.sentifi.stockprice.response.StockPriceError;
import com.sentifi.stockprice.response.TickerSymbol200DMAClosePriceListResponse;
import com.sentifi.stockprice.response.TickerSymbol200DMAClosePriceResponse;
import com.sentifi.stockprice.util.StockPriceDateUtil;
import com.sentifi.stockprice.vo.ClosePriceAvg;
import com.sentifi.stockprice.vo.TickerSymbolClosePrice;

@Service
public class TickerSymbolServiceImpl implements TickerSymbolService {

	@Autowired
	private TickerSymbolDataSource tickerSymbolDataSource; 
	
	@Override
	public TickerSymbolClosePrice getTickerSymbolClosePrice(String tickerSymbol, String startDate, String endDate)
			throws StockPriceException {
		try {
			QuandlTickerSymbol quandlTickerSymbol = tickerSymbolDataSource.getTickerSymbolDataSet(tickerSymbol, startDate, endDate);
			
			TickerSymbolClosePrice tickerSymbolClosePrice = QuandlTickerSymbol2StockPriceVOConverter
					.convertToTickerSymbolClosePrice(quandlTickerSymbol);
			
			return tickerSymbolClosePrice ;
		} catch (StockPriceException stockPriceException) {
			if(ErrorCode.QECx02.equals(stockPriceException.getErrorCode())) {
				// Quandl code is incorrect 
				stockPriceException.addErrors("tickerSymbol", tickerSymbol);
			}
			
			throw stockPriceException;
		}
	}

	@Override
	public ClosePriceAvg getTickerSymbolClosePriceAvg(String tickerSymbol, String startDate, String endDate)
			throws StockPriceException {
		
		try {
			QuandlTickerSymbol quandlTickerSymbol = tickerSymbolDataSource.getTickerSymbolDataSet(tickerSymbol, startDate, endDate);
			
			if(quandlTickerSymbol.getDataSet().getData() == null || quandlTickerSymbol.getDataSet().getData().isEmpty()) {
				// If there is no data for the start date, the first possible start date is suggested in the error message.
				StockPriceException stockPriceException = new StockPriceException(ErrorCode.QWARN01);
				stockPriceException.addErrors("startDate", "No data found with startDate = " +startDate);
				stockPriceException.addErrors("possibleStartDate", quandlTickerSymbol.getDataSet().getOldestAvailableDate());
				throw stockPriceException;
			}
			
			ClosePriceAvg closePrice200DMA = QuandlTickerSymbol2StockPriceVOConverter.convertToClosePriceAvg(quandlTickerSymbol);
			
			return closePrice200DMA;
		} catch (StockPriceException stockPriceException) {
			if(ErrorCode.QECx02.equals(stockPriceException.getErrorCode())) {
				// Quandl code is incorrect 
				stockPriceException.addErrors("tickerSymbol", tickerSymbol);
			}
			
			throw stockPriceException;
		}
	}

	@Override
	public TickerSymbol200DMAClosePriceListResponse getTickerSymbolsClosePriceAvg(List<String> tickerSymbols,
			String startDate, String endDate) throws StockPriceException {
		
		List<TickerSymbol200DMAClosePriceResponse> tickerSymbols200dma = new ArrayList<TickerSymbol200DMAClosePriceResponse>();
		List<ErrorResponse> invalidTickers  = new ArrayList<ErrorResponse>();
		
		ClosePriceAvg closePriceAvg =  null;
		ErrorResponse invalidTicker = null;
		TickerSymbol200DMAClosePriceResponse closePriceResponse = null;
		for(String tickerSymbol : tickerSymbols) {
			try {
				closePriceAvg = getTickerSymbolClosePriceAvg(tickerSymbol, startDate, endDate);
				closePriceResponse = new TickerSymbol200DMAClosePriceResponse(closePriceAvg);
				tickerSymbols200dma.add(closePriceResponse);
			} catch (StockPriceException stockPriceException) {
				ErrorCode errorCode = stockPriceException.getErrorCode();
				if(ErrorCode.QWARN01.equals(errorCode)) {
					// no data found
					// If there is no data for a ticker symbol with the start date provided, 
					// data for the first possible start date is provided back to the client.
					// try again
					String firstPossibleStartDate = stockPriceException.getErrors().get("possibleStartDate").get(0);
					String newEndDate;
					try {
						newEndDate = StockPriceDateUtil.increaseDate(startDate, 200, "yyyy-MM-dd");
						closePriceAvg = getTickerSymbolClosePriceAvg(tickerSymbol, firstPossibleStartDate, newEndDate);
						closePriceResponse = new TickerSymbol200DMAClosePriceResponse(closePriceAvg);
						tickerSymbols200dma.add(closePriceResponse);
					} catch (ParseException e) {
						e.printStackTrace();
						// log error message here
					}
					
				} else if(ErrorCode.QECx02.equals(errorCode)) {
					// Quandl code is incorrect 
					// An invalid ticker symbol generates a message in the JSON response that there is no data for it.
					StockPriceError stockPriceError = new StockPriceError(stockPriceException.getErrorCode().getCode(),
							stockPriceException.getErrorCode().getDescription());
					invalidTicker = new ErrorResponse(stockPriceError, stockPriceException.getErrors());
					
					// add invalid ticker symbol to response
					invalidTickers.add(invalidTicker);
					
				} else {
					// log error message here
				}
			}
		}
		
		TickerSymbol200DMAClosePriceListResponse response = new TickerSymbol200DMAClosePriceListResponse(tickerSymbols200dma, invalidTickers);
		return response;
	}
	
	

}
