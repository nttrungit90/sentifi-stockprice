package com.sentifi.stockprice.business.service;

import java.util.List;

import com.sentifi.stockprice.exception.StockPriceException;
import com.sentifi.stockprice.response.TickerSymbol200DMAClosePriceListResponse;
import com.sentifi.stockprice.vo.ClosePriceAvg;
import com.sentifi.stockprice.vo.TickerSymbolClosePrice;

public interface TickerSymbolService {
	public TickerSymbolClosePrice getTickerSymbolClosePrice(String tickerSymbol, String startDate, String endDate) throws StockPriceException;
	
	/**
	 * 
	 * @param tickerSymbol
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws StockPriceException <br>
	 * 	- StockPriceException(ErrorCode.QWARN01) if no data found for startDate <br>
	 *  - StockPriceException(ErrorCode,QECx02) if Quandl code is incorrect <br>
	 *  - StockPriceException(ErrorCode.QEXx03) if something went wrong <br>
	 */
	public ClosePriceAvg getTickerSymbolClosePriceAvg(String tickerSymbol, String startDate, String endDate) throws StockPriceException;
	
	public TickerSymbol200DMAClosePriceListResponse getTickerSymbolsClosePriceAvg(List<String> tickerSymbols, String startDate, String endDate) throws StockPriceException;
	
	
}
