package com.sentifi.stockprice.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.sentifi.stockprice.vo.ClosePriceAvg;
import com.sentifi.stockprice.vo.TickerSymbolClosePrice;

@Service
public class TickerSymbolServiceImpl implements TickerSymbolService {

	@Autowired
	private TickerSymbolDataSource tickerSymbolDataSource; 
	
	@Override
	public TickerSymbolClosePrice getTickerSymbolClosePrice(String tickerSymbol, String startDate, String endDate)
			throws StockPriceException {
		QuandlTickerSymbol quandlTickerSymbol = tickerSymbolDataSource.getTickerSymbolDataSet(tickerSymbol, startDate, endDate);
		
		TickerSymbolClosePrice tickerSymbolClosePrice = QuandlTickerSymbol2StockPriceVOConverter
				.convertToTickerSymbolClosePrice(quandlTickerSymbol);
		
		return tickerSymbolClosePrice ;
	}

	@Override
	public ClosePriceAvg getTickerSymbolClosePriceAvg(String tickerSymbol, String startDate, String endDate)
			throws StockPriceException {
		
		QuandlTickerSymbol quandlTickerSymbol = tickerSymbolDataSource.getTickerSymbolDataSet(tickerSymbol, startDate, endDate);
		
		if(quandlTickerSymbol.getDataSet().getData() == null || quandlTickerSymbol.getDataSet().getData().isEmpty()) {
			// If there is no data for the start date, the first possible start date is suggested in the error message.
			Map<String, List<String>> errors = new HashMap<String, List<String>>();
			List<String> errorMessages =  new ArrayList<String>();
			errorMessages.add("No data found with startDate = " +startDate);
			errorMessages.add("You can try from startDate = " + quandlTickerSymbol.getDataSet().getOldestAvailableDate());
			errors.put("startDate", errorMessages);
			StockPriceException stockPriceException = new StockPriceException(ErrorCode.QWARN01, errors);
			throw stockPriceException;
		}
		
		ClosePriceAvg closePrice200DMA = QuandlTickerSymbol2StockPriceVOConverter.convertToClosePriceAvg(quandlTickerSymbol);
		
		return closePrice200DMA;
	}

	@Override
	public TickerSymbol200DMAClosePriceListResponse getTickerSymbolsClosePriceAvg(List<String> tickerSymbols,
			String startDate, String endDate) throws StockPriceException {
		
		List<TickerSymbol200DMAClosePriceResponse> tickerSymbols200dma = new ArrayList<TickerSymbol200DMAClosePriceResponse>();
		List<ErrorResponse> invalidTickers  = new ArrayList<ErrorResponse>();
		List<ErrorResponse> noDataFoundTickers  = new ArrayList<ErrorResponse>();
		
		ClosePriceAvg closePriceAvg =  null;
		ErrorResponse invalidTicker = null;
		ErrorResponse noDataFoundTicker = null;
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
					StockPriceError stockPriceError = new StockPriceError(stockPriceException.getErrorCode().getCode(),
							stockPriceException.getErrorCode().getDescription());
					noDataFoundTicker = new ErrorResponse(stockPriceError, stockPriceException.getErrors());
					noDataFoundTicker.addErrors("tickerSymbol", tickerSymbol);
					
					// add no data found ticker symbol to response
					noDataFoundTickers.add(noDataFoundTicker);

				} else if(ErrorCode.QECx02.equals(errorCode)) {
					// Quandl code is incorrect 
					// An invalid ticker symbol generates a message in the JSON response that there is no data for it.
					StockPriceError stockPriceError = new StockPriceError(stockPriceException.getErrorCode().getCode(),
							stockPriceException.getErrorCode().getDescription());
					invalidTicker = new ErrorResponse(stockPriceError, stockPriceException.getErrors());
					invalidTicker.addErrors("tickerSymbol", tickerSymbol);
					
					// add invalid ticker symbol to response
					invalidTickers.add(invalidTicker);
					
				} else {
					// log error message here
				}
			}
		}
		
		TickerSymbol200DMAClosePriceListResponse response = new TickerSymbol200DMAClosePriceListResponse(tickerSymbols200dma, invalidTickers, noDataFoundTickers);
		return response;
	}
	
	

}
