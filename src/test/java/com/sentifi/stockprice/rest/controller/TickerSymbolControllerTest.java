package com.sentifi.stockprice.rest.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import com.sentifi.StockPriceService;
import com.sentifi.stockprice.business.service.TickerSymbolService;
import com.sentifi.stockprice.exception.ErrorCode;
import com.sentifi.stockprice.exception.StockPriceException;
import com.sentifi.stockprice.vo.ClosePrice;
import com.sentifi.stockprice.vo.TickerSymbolClosePrice;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockPriceService.class)
public class TickerSymbolControllerTest {

private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),                        
			Charset.forName("utf8")                     
        );
	
	@Autowired
    private WebApplicationContext wac;
	
	@MockBean
	private TickerSymbolService tickerSymbolServiceMock;
	

	@Before
	public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        
	}

	// ############ Start test cases for requirement 1 ######################
	@Test
	public void verifyGetTickerSymbolClosePriceAllParamsSuccess() throws Exception {
		/*
		Expected returned Json
		{
		    "Prices": [
			    {
				    "Ticker": "GE",
				    "DateClose": ["2017-02-13", "30.04"],
				    "DateClose": ["2017-02-14", "30.28"],
				    "DateClose": ["2017-02-15", "30.35"]
			    }
		    ]
		}
		 */
		
		//prepare test data
		String tickerSymbol = "GE";
		String startDate = "2017-02-15";
		String endDate = "2017-02-15";
		
		TickerSymbolClosePrice tickerSymbolClosePrice = new TickerSymbolClosePrice();
		tickerSymbolClosePrice.setTicker(tickerSymbol);
		List<ClosePrice> closeDates  = Arrays.asList(new ClosePrice("2017-02-15", 30.35));
		tickerSymbolClosePrice.setCloseDates(closeDates);
		
		// mock test data
        when(tickerSymbolServiceMock.getTickerSymbolClosePrice(tickerSymbol, startDate, endDate)).thenReturn(tickerSymbolClosePrice);
 
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/");
		builder.path(tickerSymbol + "/closePrice"); 
		builder.queryParam("startDate", startDate);
		builder.queryParam("endDate", endDate);
		URI uri = builder.build().encode().toUri();

		// execute test
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Prices.[0].Ticker", is(tickerSymbol)))
                .andExpect(jsonPath("$.Prices.[0].DateClose[0]", is("2017-02-15")));
 
        verify(tickerSymbolServiceMock, times(1)).getTickerSymbolClosePrice(tickerSymbol, startDate, endDate);
        verifyNoMoreInteractions(tickerSymbolServiceMock);
        
	}
	
	
	@Test
	/*
	 * An invalid ticker symbol returns a easy to understand error and HTTP Code of 404
	 */
	public void verifyGetTickerSymbolClosePriceInvalidTickerSymbol() throws Exception {
		/*
		Expected returned Json
		{
		    "errors": {},
		    "stockPriceError": {
		        "code": "QECx02",
		        "message": "You have submitted an incorrect Quandl code. Please check your Quandl codes and try again."
	    	}
		}
		*/
		
		//prepare test data
		String tickerSymbol = "invalidTickerSymbol";
		String startDate = "2017-02-15";
		String endDate = "2017-02-15";
		StockPriceException stockPriceException = new StockPriceException(ErrorCode.QECx02);
	
		
		// mock test data
		when(tickerSymbolServiceMock.getTickerSymbolClosePrice(eq(tickerSymbol), anyString(), anyString())).thenThrow(stockPriceException);
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/");
		builder.path(tickerSymbol + "/closePrice"); 
		builder.queryParam("startDate", startDate);
		builder.queryParam("endDate", endDate);
		URI uri = builder.build().encode().toUri();

		// execute test
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.stockPriceError.code", is("QECx02")));
 
        verify(tickerSymbolServiceMock, times(1)).getTickerSymbolClosePrice(tickerSymbol, startDate, endDate);
        verifyNoMoreInteractions(tickerSymbolServiceMock);
	}
	
	// ############ End test cases for requirement 1 ######################
	
	@Test
	public void verifyGetTickerSymbol200DayMovingAvgClosePrice() throws Exception {
		
	}
	
	@Test
	public void verifyGetTickerSymbol200DMA() throws Exception {
		
	}
	
	
}
