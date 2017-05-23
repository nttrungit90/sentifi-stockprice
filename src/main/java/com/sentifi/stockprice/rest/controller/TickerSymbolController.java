package com.sentifi.stockprice.rest.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sentifi.stockprice.business.service.TickerSymbolService;
import com.sentifi.stockprice.exception.ErrorCode;
import com.sentifi.stockprice.exception.StockPriceException;
import com.sentifi.stockprice.resolver.StockPriceExceptionToResponseEntityResolver;
import com.sentifi.stockprice.response.ErrorResponse;
import com.sentifi.stockprice.response.TickerSymbol200DMAClosePriceListResponse;
import com.sentifi.stockprice.response.TickerSymbol200DMAClosePriceResponse;
import com.sentifi.stockprice.response.TickerSymbolClosePriceResponse;
import com.sentifi.stockprice.util.StockPriceDateUtil;
import com.sentifi.stockprice.vo.ClosePriceAvg;
import com.sentifi.stockprice.vo.TickerSymbolClosePrice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Spring MVC controller for TickerSymbol management.
 */

@Api(value="ticker symbol api", description = "Operations pertaining to ticker symbol")
@RestController
@RequestMapping("/api")
public class TickerSymbolController {

	@Resource
	private TickerSymbolService tickerSymbolService;
	
	@Resource
	private StockPriceExceptionToResponseEntityResolver exceptionToErrorResponseResolver;
	
	@RequestMapping( value="/v2/{tickersymbol}/closePrice",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@Cacheable(value="closePrices")
	public ResponseEntity<?> getTickerSymbolClosePrice(
			@PathVariable(name = "tickersymbol", required = true) String tickersymbol,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate) {
		
		try {
			// validate input
			validateStartDateAndEndDate(startDate, endDate);
			
			// pass validation, call service to handle business logic 
			TickerSymbolClosePriceResponse tickerSymbolClosePriceResponse = new TickerSymbolClosePriceResponse();
			TickerSymbolClosePrice tickerSymbolClosePrice = tickerSymbolService.getTickerSymbolClosePrice(tickersymbol, startDate, endDate);
			List<TickerSymbolClosePrice> closePrices = new ArrayList<TickerSymbolClosePrice>();
			closePrices.add(tickerSymbolClosePrice);
			tickerSymbolClosePriceResponse.setPrices(closePrices);
			
			ResponseEntity<TickerSymbolClosePriceResponse> responseEntity = new ResponseEntity<>(tickerSymbolClosePriceResponse,
	                HttpStatus.OK);
			
			return responseEntity;
		} catch (StockPriceException stockPriceException) {
			// if there is any exception, resolve it and response with meaningful error message and http status code to user using a centralized exception resolver
			ResponseEntity<ErrorResponse> responseEntity = exceptionToErrorResponseResolver.resolve(stockPriceException);
			return responseEntity;
		}
		
	}
	
	@RequestMapping( value="/v2/{tickersymbol}/200dma",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<?> getTickerSymbol200DayMovingAvgClosePrice(
			@PathVariable(name = "tickersymbol", required = true) String tickersymbol,
			@RequestParam(name = "startDate", required = true) String startDate) {
		
		try {
			// validate input
			if(startDate != null && !startDate.isEmpty()) {
				if(!StockPriceDateUtil.isThisDateValid(startDate, "yyyy-MM-dd")) {
					Map<String, List<String>> errors = new HashMap<String, List<String>>();
					List<String> errorMessages =  new ArrayList<String>();
					errorMessages.add("startDate is invalid format. The format must be yyyy-MM-dd");
					errors.put("startDate", errorMessages);
					throw new StockPriceException(ErrorCode.QESx04, errors);
				}
			}
			
			String endDate;
			try {
				endDate = StockPriceDateUtil.increaseDate(startDate, 200, "yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
				// throw default error
				StockPriceException stockPriceException = new StockPriceException(e.getMessage(), ErrorCode.QEXx03);
				throw stockPriceException;
			}
			
			// pass validation, call service to handle business logic 
			TickerSymbol200DMAClosePriceResponse tickerSymbol200dmaClosePriceResponse = new TickerSymbol200DMAClosePriceResponse();
			ClosePriceAvg closePrice200DMA = tickerSymbolService.getTickerSymbolClosePriceAvg(tickersymbol, startDate, endDate);
			tickerSymbol200dmaClosePriceResponse.setClosePrice200DMA(closePrice200DMA);
			
			ResponseEntity<TickerSymbol200DMAClosePriceResponse> responseEntity = new ResponseEntity<>(tickerSymbol200dmaClosePriceResponse,
	                HttpStatus.OK);
			
			return responseEntity;
		} catch (StockPriceException stockPriceException) {
			// if there is any exception, resolve it and response with meaningful error message and http status code to user using a centralized exception resolver
			ResponseEntity<ErrorResponse> responseEntity = exceptionToErrorResponseResolver.resolve(stockPriceException);
			return responseEntity;
		}
		
	}
	
	
	@ApiOperation(value = "Provide the 200 day moving average price for a up to 1000 ticker symbols beginning with a start date."
			+ "An invalid ticker symbol generates a message in the JSON response that the ticker is invalid."
			+ "If there is no data for a ticker symbol with the start date provided, data for the first possible start date is provided back to the client.", 
			
			
			
			response = TickerSymbol200DMAClosePriceListResponse.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully query for 200 day moving average price of ticker symbols"),
	        @ApiResponse(code = 404, message = "In case start date is in valid format")
		}
	)
	
	@RequestMapping( value="/v2/200dma",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<?> getMutipleTickerSymbol200DMA(
			@RequestParam(name = "tickerSysmbols", required = true) List<String> tickerSysmbols,
			@RequestParam(name = "startDate", required = true) String startDate) {
		
		try {
			// validate input
			if(startDate != null && !startDate.isEmpty()) {
				if(!StockPriceDateUtil.isThisDateValid(startDate, "yyyy-MM-dd")) {
					Map<String, List<String>> errors = new HashMap<String, List<String>>();
					List<String> errorMessages =  new ArrayList<String>();
					errorMessages.add("startDate is invalid format. The format must be yyyy-MM-dd");
					errors.put("startDate", errorMessages);
					throw new StockPriceException(ErrorCode.QESx04, errors);
				}
			}
			
			String endDate;
			try {
				endDate = StockPriceDateUtil.increaseDate(startDate, 200, "yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
				// throw default error
				StockPriceException stockPriceException = new StockPriceException(e.getMessage(), ErrorCode.QEXx03);
				throw stockPriceException;
			}
			
			// pass validation, call service to handle business logic 
			TickerSymbol200DMAClosePriceListResponse closePriceListResponse = tickerSymbolService.getTickerSymbolsClosePriceAvg(tickerSysmbols, startDate, endDate);
			
			ResponseEntity<TickerSymbol200DMAClosePriceListResponse> responseEntity = new ResponseEntity<>(closePriceListResponse,
	                HttpStatus.OK);
			
			return responseEntity;
		} catch (StockPriceException stockPriceException) {
			// if there is any exception, resolve it and response with meaningful error message and http status code to user using a centralized exception resolver
			ResponseEntity<ErrorResponse> responseEntity = exceptionToErrorResponseResolver.resolve(stockPriceException);
			return responseEntity;
		}
		
	}


	private void validateStartDateAndEndDate(String startDate, String endDate) throws StockPriceException {
		if(startDate != null && !startDate.isEmpty()) {
			if(!StockPriceDateUtil.isThisDateValid(startDate, "yyyy-MM-dd")) {
				Map<String, List<String>> errors = new HashMap<String, List<String>>();
				List<String> errorMessages =  new ArrayList<String>();
				errorMessages.add("startDate is invalid format. The format must be yyyy-MM-dd");
				errors.put("startDate", errorMessages);
				throw new StockPriceException(ErrorCode.QESx04, errors);
			}
		}
		
		if(endDate != null && !endDate.isEmpty()) {
			if(!StockPriceDateUtil.isThisDateValid(endDate, "yyyy-MM-dd")) {
				Map<String, List<String>> errors = new HashMap<String, List<String>>();
				List<String> errorMessages =  new ArrayList<String>();
				errorMessages.add("endDate is invalid format. The format must be yyyy-MM-dd");
				errors.put("endDate", errorMessages);
				throw new StockPriceException(ErrorCode.QESx04, errors);
			}
		}
		
		if((startDate != null && !startDate.isEmpty()) && (endDate != null && !endDate.isEmpty())) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date startDateObject = df.parse(startDate);
				Date endDateObject = df.parse(endDate);
				if(startDateObject.compareTo(endDateObject) > 0) {
					Map<String, List<String>> errors = new HashMap<String, List<String>>();
					List<String> errorMessages =  new ArrayList<String>();
					errorMessages.add("startDate should not exceed endDate");
					errors.put("startDate", errorMessages);
					throw new StockPriceException(ErrorCode.QESx04, errors);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
}
