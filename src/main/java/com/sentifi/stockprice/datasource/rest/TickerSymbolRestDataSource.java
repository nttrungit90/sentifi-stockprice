package com.sentifi.stockprice.datasource.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentifi.stockprice.datasource.TickerSymbolDataSource;
import com.sentifi.stockprice.datasource.rest.model.QuandlErrorResponse;
import com.sentifi.stockprice.datasource.rest.model.QuandlTickerSymbol;
import com.sentifi.stockprice.exception.ErrorCode;
import com.sentifi.stockprice.exception.StockPriceException;
import com.sentifi.stockprice.resolver.QuandlError2ErrorCodeResolver;

@Component
public class TickerSymbolRestDataSource implements TickerSymbolDataSource{
	private static String REST_URL = "https://www.quandl.com/api/v3/datasets/WIKI/";
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public QuandlTickerSymbol getTickerSymbolDataSet(String tickerSymbol, String startDate, String endDate) throws StockPriceException {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(REST_URL);
		builder.path(tickerSymbol + ".json"); 
		builder.queryParam("start_date", startDate);
		builder.queryParam("end_date", endDate);
		builder.queryParam("column_index", 4);
		builder.queryParam("order", "asc");
		builder.queryParam("api_key", "8m6U4326RcMRoFZxGdgf");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<QuandlTickerSymbol> response = restTemplate
					  .exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, QuandlTickerSymbol.class);
			QuandlTickerSymbol quandlTickerSymbol = response.getBody();
			
			return quandlTickerSymbol;
			
		} catch (HttpClientErrorException e) {
			String errorMessage = e.getResponseBodyAsString();
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				QuandlErrorResponse quandlErrorResponse = objectMapper.readValue(errorMessage, QuandlErrorResponse.class);
				
				StockPriceException stockPriceException = new StockPriceException(e.getMessage(), 
						QuandlError2ErrorCodeResolver.resolve(quandlErrorResponse.getQuandlError()), 
						quandlErrorResponse.getErrors());
				throw stockPriceException;
			} catch (StockPriceException stockPriceException) {
				throw stockPriceException;
			} catch (Exception convertException) {
				// throw default error
				StockPriceException stockPriceException = new StockPriceException(e.getMessage(), ErrorCode.QEXx03);
				throw stockPriceException;
			}
		}
	}
		
		
}
