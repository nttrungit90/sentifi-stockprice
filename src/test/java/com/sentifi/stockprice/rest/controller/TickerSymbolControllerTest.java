package com.sentifi.stockprice.rest.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import com.sentifi.StockPriceService;
import com.sentifi.stockprice.business.service.TickerSymbolService;
import com.sentifi.stockprice.exception.ErrorCode;
import com.sentifi.stockprice.exception.StockPriceException;
import com.sentifi.stockprice.response.ErrorResponse;
import com.sentifi.stockprice.response.StockPriceError;
import com.sentifi.stockprice.response.TickerSymbol200DMAClosePriceListResponse;
import com.sentifi.stockprice.response.TickerSymbol200DMAClosePriceResponse;
import com.sentifi.stockprice.util.StockPriceDateUtil;
import com.sentifi.stockprice.vo.ClosePrice;
import com.sentifi.stockprice.vo.ClosePriceAvg;
import com.sentifi.stockprice.vo.TickerSymbolClosePrice;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StockPriceService.class)
public class TickerSymbolControllerTest {

private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),                        
			Charset.forName("utf8"));
	
	@Autowired
    private WebApplicationContext wac;
	
	@MockBean
	private TickerSymbolService tickerSymbolServiceMock;
	

	@Before
	public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        
	}

	// ############ Start test cases for requirement 1 ######################
	/*
	 * This test case test with all parameters of the API /api/v2/{tickerSymbol}
	 */
	@Test
	public void verifyGetTickerSymbolClosePrice() throws Exception {
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
	
	
	
	/*
	 * An invalid ticker symbol returns a easy to understand error and HTTP Code of 404
	 */
	@Test
	public void verifyGetTickerSymbolClosePriceInvalidTickerSymbol() throws Exception {
		/*
		Expected returned Json
		{
		    "errors": {
		        "tickerSymbol": ["invalidTickerSymbol"]
		    },
		    "stockPriceError": {
		        "code": "QECx02",
		        "message": "You have submitted an incorrect Quandl code. Please check your Quandl codes and try again."
		    }
		}
		*/
		
		//prepare test data
		String tickerSymbol = "invalidTickerSymbol";
		String startDate = "2017-02-13";
		String endDate = "2017-02-15";
		StockPriceException stockPriceException = new StockPriceException(ErrorCode.QECx02);
		stockPriceException.addErrors("tickerSymbol", tickerSymbol);
		
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
                .andExpect(jsonPath("$.errors.tickerSymbol[0]", is(tickerSymbol)))
                .andExpect(jsonPath("$.stockPriceError.code", is("QECx02")))
                .andExpect(jsonPath("$.stockPriceError.message", is("You have submitted an incorrect Quandl code. Please check your Quandl codes and try again.")));
 
        verify(tickerSymbolServiceMock, times(1)).getTickerSymbolClosePrice(tickerSymbol, startDate, endDate);
        verifyNoMoreInteractions(tickerSymbolServiceMock);
	}
	
	
	
	/*
	 * An invalid start time should return an easy to understand error and HTTP Code of 404
	 */
	@Test
	public void verifyGetTickerSymbolClosePriceInvalidStartTime() throws Exception {
		/*
		Expected returned Json
		{
		    "errors": {
		        "startDate": ["startDate is invalid format. The format must be yyyy-MM-dd"]
		    },
		    "stockPriceError": {
		        "code": "QESx04",
		        "message": "You have submitted incorrect query parameters. Please check your API call syntax and try again."
		    }
		}
		*/
		
		//prepare test data
		String tickerSymbol = "GE";
		String startDate = "2017-02-xx";
		String endDate = "2017-02-13";
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/");
		builder.path(tickerSymbol + "/closePrice"); 
		builder.queryParam("startDate", startDate);
		builder.queryParam("endDate", endDate);
		URI uri = builder.build().encode().toUri();

		// execute test
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.stockPriceError.code", is("QESx04")))
                .andExpect(jsonPath("$.stockPriceError.message", is("You have submitted incorrect query parameters. Please check your API call syntax and try again.")))
                .andExpect(jsonPath("$.errors.startDate[0]", is("startDate is invalid format. The format must be yyyy-MM-dd")));
 
        verify(tickerSymbolServiceMock, times(0)).getTickerSymbolClosePrice(tickerSymbol, startDate, endDate);
	}
	
	
	/*
	 * An invalid end time should return an easy to understand error and HTTP Code of 404
	 */
	@Test
	public void verifyGetTickerSymbolClosePriceInvalidEndDate() throws Exception {
		/*
		Expected returned Json
		{
		    "errors": {
		        "startDate": ["endDate is invalid format. The format must be yyyy-MM-dd"]
		    },
		    "stockPriceError": {
		        "code": "QESx04",
		        "message": "You have submitted incorrect query parameters. Please check your API call syntax and try again."
		    }
		}
		*/
		
		//prepare test data
		String tickerSymbol = "GE";
		String startDate = "2017-02-15";
		String endDate = "2017-02-xx";
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/");
		builder.path(tickerSymbol + "/closePrice"); 
		builder.queryParam("startDate", startDate);
		builder.queryParam("endDate", endDate);
		URI uri = builder.build().encode().toUri();

		// execute test
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.stockPriceError.code", is("QESx04")))
                .andExpect(jsonPath("$.stockPriceError.message", is("You have submitted incorrect query parameters. Please check your API call syntax and try again.")))
                .andExpect(jsonPath("$.errors.endDate[0]", is("endDate is invalid format. The format must be yyyy-MM-dd")));
 
        verify(tickerSymbolServiceMock, times(0)).getTickerSymbolClosePrice(tickerSymbol, startDate, endDate);
	}
	
	/*
	 * An invalid time range should return a easy to understand error and HTTP Code of 404
	 */
	@Test
	public void verifyGetTickerSymbolClosePriceInvalidTimeRange() throws Exception {
		/*
		Expected returned Json
		{
		    "errors": {
		        "startDate": ["startDate should not exceed endDate"]
		    },
		    "stockPriceError": {
		        "code": "QESx04",
		        "message": "You have submitted incorrect query parameters. Please check your API call syntax and try again."
		    }
		}
		*/
		
		//prepare test data
		String tickerSymbol = "GE";
		String startDate = "2017-02-15";
		String endDate = "2017-02-13";
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/");
		builder.path(tickerSymbol + "/closePrice"); 
		builder.queryParam("startDate", startDate);
		builder.queryParam("endDate", endDate);
		URI uri = builder.build().encode().toUri();

		// execute test
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.stockPriceError.code", is("QESx04")))
                .andExpect(jsonPath("$.stockPriceError.message", is("You have submitted incorrect query parameters. Please check your API call syntax and try again.")))
                .andExpect(jsonPath("$.errors.startDate[0]", is("startDate should not exceed endDate")));
 
        verify(tickerSymbolServiceMock, times(0)).getTickerSymbolClosePrice(tickerSymbol, startDate, endDate);
	}
	// ############ End test cases for requirement 1 ######################
	
	
	// ############ Start test cases for requirement 2 ######################
	
	/*
	 * This test for the success case of get 200dma of a ticker symbol with a valid and has data start time
	 */
	@Test
	public void verifyGetTickerSymbol200DMA() throws Exception {
		/*
		 * Expected Json
		{
		    "200dma": {
		        "Ticker": "GE",
		        "Avg": "29.577599999999997"
		    }
		}
		*/
		
		String tickerSymbol = "GE";
		String startDate = "2017-02-02";
		String endDate = StockPriceDateUtil.increaseDate(startDate, 200, "yyyy-MM-dd");
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/");
		builder.path(tickerSymbol + "/200dma"); 
		builder.queryParam("startDate", startDate);
		URI uri = builder.build().encode().toUri();

		ClosePriceAvg closePrice200DMA = new ClosePriceAvg(tickerSymbol, "29.577599999999997");
		// mock test data
		when(tickerSymbolServiceMock.getTickerSymbolClosePriceAvg(eq(tickerSymbol), eq(startDate), eq(endDate))).thenReturn(closePrice200DMA);
				
		// execute test
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.200dma.Ticker", is("GE")))
                .andExpect(jsonPath("$.200dma.Avg", is("29.577599999999997")));
 
        verify(tickerSymbolServiceMock, times(1)).getTickerSymbolClosePriceAvg(tickerSymbol, startDate, endDate);
	}
	
	/*
	 * An invalid ticker symbol returns a easy to understand error and HTTP Code of 404
	 */
	@Test
	public void verifyGetTickerSymbol200DMAInvalidTickerSymbol() throws Exception {
		/*
		Expected returned Json
		{
		    "errors": {
		        "tickerSymbol": ["invalidTickerSymbol"]
		    },
		    "stockPriceError": {
		        "code": "QECx02",
		        "message": "You have submitted an incorrect Quandl code. Please check your Quandl codes and try again."
		    }
		}
		*/
		
		//prepare test data
		String tickerSymbol = "invalidTickerSymbol";
		String startDate = "2017-02-13";
		String endDate = StockPriceDateUtil.increaseDate(startDate, 200, "yyyy-MM-dd");
		
		StockPriceException stockPriceException = new StockPriceException(ErrorCode.QECx02);
		stockPriceException.addErrors("tickerSymbol", tickerSymbol);
		
		// mock test data
		when(tickerSymbolServiceMock.getTickerSymbolClosePriceAvg(eq(tickerSymbol), eq(startDate), eq(endDate))).thenThrow(stockPriceException);
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/");
		builder.path(tickerSymbol + "/200dma"); 
		builder.queryParam("startDate", startDate);
		URI uri = builder.build().encode().toUri();

		// execute test
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.errors.tickerSymbol[0]", is(tickerSymbol)))
                .andExpect(jsonPath("$.stockPriceError.code", is("QECx02")))
                .andExpect(jsonPath("$.stockPriceError.message", is("You have submitted an incorrect Quandl code. Please check your Quandl codes and try again.")));
 
        verify(tickerSymbolServiceMock, times(1)).getTickerSymbolClosePriceAvg(tickerSymbol, startDate, endDate);
        verifyNoMoreInteractions(tickerSymbolServiceMock);
	}
	
	
	/*
	 * An invalid start time should return an easy to understand error and HTTP Code of 404
	 */
	@Test
	public void verifyGetTickerSymbol200DMAInvalidStartTime() throws Exception {
		/*
		Expected returned Json
		{
		    "errors": {
		        "startDate": ["startDate is invalid format. The format must be yyyy-MM-dd"]
		    },
		    "stockPriceError": {
		        "code": "QESx04",
		        "message": "You have submitted incorrect query parameters. Please check your API call syntax and try again."
		    }
		}
		*/
		
		//prepare test data
		String tickerSymbol = "GE";
		String startDate = "2017-02-xx";
		String endDate = "2017-09-01";
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/");
		builder.path(tickerSymbol + "/200dma"); 
		builder.queryParam("startDate", startDate);
		URI uri = builder.build().encode().toUri();

		// execute test
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.stockPriceError.code", is("QESx04")))
                .andExpect(jsonPath("$.stockPriceError.message", is("You have submitted incorrect query parameters. Please check your API call syntax and try again.")))
                .andExpect(jsonPath("$.errors.startDate[0]", is("startDate is invalid format. The format must be yyyy-MM-dd")));
 
        verify(tickerSymbolServiceMock, times(0)).getTickerSymbolClosePriceAvg(tickerSymbol, startDate, endDate);
	}
	
	
	/*
	 * If there is no data for the start date, the first possible start date is suggested in the error message.
	 */
	@Test
	public void verifyGetTickerSymbol200DMANoDataWithStartTime() throws Exception {
		/*
		Expected returned Json
		{
		    "errors": {
		    	"possibleStartDate": ["1962-01-02"],
		        "startDate": ["No data found with startDate = 2018-2-3"]
		    },
		    "stockPriceError": {
		        "code": "QWARN01",
		        "message": "No data found for your request. Please try with other value of request parameters."
		    }
		}
		*/
		
		//prepare test data
		String tickerSymbol = "GE";
		String startDate = "2018-2-3";
		String endDate = StockPriceDateUtil.increaseDate(startDate, 200, "yyyy-MM-dd");
		
		StockPriceException stockPriceException = new StockPriceException(ErrorCode.QWARN01);
		stockPriceException.addErrors("startDate", "No data found with startDate = " +startDate);
		stockPriceException.addErrors("possibleStartDate", "1962-01-02");
		
		// mock test data
		when(tickerSymbolServiceMock.getTickerSymbolClosePriceAvg(eq(tickerSymbol), eq(startDate), eq(endDate))).thenThrow(stockPriceException);
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/");
		builder.path(tickerSymbol + "/200dma"); 
		builder.queryParam("startDate", startDate);
		URI uri = builder.build().encode().toUri();

		// execute test
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON));
        resultActions
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.stockPriceError.code", is("QWARN01")))
                .andExpect(jsonPath("$.stockPriceError.message", is("No data found for your request. Please try with other value of request parameters.")))
                .andExpect(jsonPath("$.errors.startDate[0]", is("No data found with startDate = 2018-2-3")))
                .andExpect(jsonPath("$.errors.possibleStartDate[0]", is("1962-01-02")));
 
        verify(tickerSymbolServiceMock, times(1)).getTickerSymbolClosePriceAvg(tickerSymbol, startDate, endDate);
	}
	
	// ############ End test cases for requirement 2 ######################
	
	
	// ############ Start test cases for requirement 3 ######################
	
	/*
	 * An invalid ticker symbol generates a message in the JSON response that there is no data for it.
	 * If there is no data for a ticker symbol with the start date provided, 
	 * data for the first possible start date is provided back to the client.
	 */
	@Test
	public void verifyGetMutipleTickerSymbol200DMA() throws Exception {
		/*
		 Expected json
		 {
		    "tickerSymbols200dma": [{
		        "200dma": {
		            "Ticker": "GE",
		            "Avg": "58.84936366374991"
		        }
		    }, {
		        "200dma": {
		            "Ticker": "FE",
		            "Avg": "39.80999287604318"
		        }
		    }],
		    "invalidTickers": [{
		        "errors": {
		            "tickerSymbol": ["InvalidTicker"]
		        },
		        "stockPriceError": {
		            "code": "QECx02",
		            "message": "You have submitted an incorrect Quandl code. Please check your Quandl codes and try again."
		        }
		    }]
		}
		 */
		
		
		String startDate = "2017-02-02";
		String endDate = StockPriceDateUtil.increaseDate(startDate, 200, "yyyy-MM-dd");
		List<String> tickerSymbols = Arrays.asList("GE","FE","InvalidTicker");
		
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v2/200dma");
		builder.queryParam("startDate", startDate);
		builder.queryParam("tickerSysmbols", "GE,FE,InvalidTicker");
		URI uri = builder.build().encode().toUri();

		// mock test data
		
		List<TickerSymbol200DMAClosePriceResponse> tickerSymbols200dma = new ArrayList<TickerSymbol200DMAClosePriceResponse>();
		TickerSymbol200DMAClosePriceResponse closePriceResponse1 = new TickerSymbol200DMAClosePriceResponse(new ClosePriceAvg("GE", "58.84936366374991"));
		TickerSymbol200DMAClosePriceResponse closePriceResponse2 = new TickerSymbol200DMAClosePriceResponse(new ClosePriceAvg("FE", "39.80999287604318"));
		tickerSymbols200dma.add(closePriceResponse1);
		tickerSymbols200dma.add(closePriceResponse2);
		
		List<ErrorResponse> invalidTickers  = new ArrayList<ErrorResponse>();
		StockPriceError stockPriceError = new StockPriceError("QECx02", "You have submitted an incorrect Quandl code. Please check your Quandl codes and try again.");
		ErrorResponse errorResponse = new ErrorResponse(stockPriceError);
		errorResponse.addErrors("tickerSymbol", "InvalidTicker");
		invalidTickers.add(errorResponse);
		
		TickerSymbol200DMAClosePriceListResponse closePriceListResponse = new TickerSymbol200DMAClosePriceListResponse(tickerSymbols200dma, invalidTickers);
		
		when(tickerSymbolServiceMock.getTickerSymbolsClosePriceAvg(eq(tickerSymbols), eq(startDate), eq(endDate))).thenReturn(closePriceListResponse);
				
		// execute test
        mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tickerSymbols200dma[0].200dma.Ticker", is("GE")))
                .andExpect(jsonPath("$.tickerSymbols200dma[0].200dma.Avg", is("58.84936366374991")))
                
                .andExpect(jsonPath("$.tickerSymbols200dma[1].200dma.Ticker", is("FE")))
                .andExpect(jsonPath("$.tickerSymbols200dma[1].200dma.Avg", is("39.80999287604318")))
                
                .andExpect(jsonPath("$.invalidTickers[0].errors.tickerSymbol[0]", is("InvalidTicker")))
                .andExpect(jsonPath("$.invalidTickers[0].stockPriceError.code", is("QECx02")))
                .andExpect(jsonPath("$.invalidTickers[0].stockPriceError.message", is("You have submitted an incorrect Quandl code. Please check your Quandl codes and try again.")));
 
        verify(tickerSymbolServiceMock, times(1)).getTickerSymbolsClosePriceAvg(tickerSymbols, startDate, endDate);
		
	}
	
	// ############ End test cases for requirement 3 ######################
	
}
