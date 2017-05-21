package com.sentifi.stockprice.datasource.rest.converter;

import java.util.ArrayList;
import java.util.List;

import com.sentifi.stockprice.datasource.rest.model.DataSet;
import com.sentifi.stockprice.datasource.rest.model.QuandlTickerSymbol;
import com.sentifi.stockprice.vo.ClosePrice;
import com.sentifi.stockprice.vo.ClosePriceAvg;
import com.sentifi.stockprice.vo.TickerSymbolClosePrice;

public class QuandlTickerSymbol2StockPriceVOConverter {
	public static TickerSymbolClosePrice convertToTickerSymbolClosePrice(QuandlTickerSymbol quandlTickerSymbol) {
		TickerSymbolClosePrice tickerSymbolClosePrice = new TickerSymbolClosePrice();
		
		DataSet dataSet = quandlTickerSymbol.getDataSet();
		// set ticker symbol
		tickerSymbolClosePrice.setTicker(dataSet.getDatasetCode());

		// set closeDates
		List<String> columnNames = dataSet.getColumnNames();
		List<List<Object>> datas = dataSet.getData();
		int dateIndex = columnNames.indexOf(DataSet.COLUMN_DATE);
		int closeIndex = columnNames.indexOf(DataSet.COLUMN_CLOSE);
		
		List<ClosePrice> closePrices = new ArrayList<ClosePrice>();
		ClosePrice closePrice = null;
		for(List<Object> dataItem : datas) {
			closePrice = new ClosePrice();
			closePrice.setDate(dataItem.get(dateIndex).toString());
			closePrice.setClose((Double)dataItem.get(closeIndex));
			closePrices.add(closePrice);
		}
		tickerSymbolClosePrice.setCloseDates(closePrices);
		
		return tickerSymbolClosePrice;
	}
	
	public static ClosePriceAvg convertToClosePriceAvg(QuandlTickerSymbol quandlTickerSymbol) {
		ClosePriceAvg closePrice200DMA = new ClosePriceAvg();
		
		DataSet dataSet = quandlTickerSymbol.getDataSet();
		// set ticker symbol
		closePrice200DMA.setTicker(dataSet.getDatasetCode());

		// caculate avg close price
		List<String> columnNames = dataSet.getColumnNames();
		List<List<Object>> datas = dataSet.getData();
		int closeIndex = columnNames.indexOf(DataSet.COLUMN_CLOSE);
		
		if(!datas.isEmpty()) {
			double sum = 0;
			for(List<Object> dataItem : datas) {
				sum = sum + (Double)dataItem.get(closeIndex);
			}
			int dataSize = datas.size();
			double avg200DayClosePrice = sum/dataSize;
			closePrice200DMA.setAvg(String.valueOf(avg200DayClosePrice));
		} else {
			closePrice200DMA.setAvg("0");
		}
			
		return closePrice200DMA;
	}
}
