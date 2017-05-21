package com.sentifi.stockprice.datasource;

import com.sentifi.stockprice.datasource.rest.model.QuandlTickerSymbol;
import com.sentifi.stockprice.exception.StockPriceException;

public interface TickerSymbolDataSource {
	public QuandlTickerSymbol getTickerSymbolDataSet(String tickerSymbol, String startDate, String endDate) throws StockPriceException;
}
