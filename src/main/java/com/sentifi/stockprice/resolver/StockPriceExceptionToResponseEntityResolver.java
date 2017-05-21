package com.sentifi.stockprice.resolver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sentifi.stockprice.exception.ErrorCode;
import com.sentifi.stockprice.exception.StockPriceException;
import com.sentifi.stockprice.response.ErrorResponse;
import com.sentifi.stockprice.response.StockPriceError;

@Component
/**
 * This class resolve 'StockPriceException' to ResponseEntity with meaningful error message and http status code
 * @author admin
 *
 */
public class StockPriceExceptionToResponseEntityResolver {
	
	/**
	 * Resolve the StockPriceException to ResponseEntity<ErrorResponse> with meaningful error message and http status code
	 * @param stockPriceException
	 * @return ResponseEntity<ErrorResponse> with meaningful error message and http status code
	 */
	public ResponseEntity<ErrorResponse> resolve(StockPriceException stockPriceException) {
		ResponseEntity<ErrorResponse> responseEntity = null;
		
		StockPriceError stockPriceError = new StockPriceError(stockPriceException.getErrorCode().getCode(),
				stockPriceException.getErrorCode().getDescription());
		ErrorResponse errorResponse = new ErrorResponse(stockPriceError, stockPriceException.getErrors());
		// update HttpStatus code for each message code
		if(ErrorCode.QEXx03.equals(stockPriceException.getErrorCode())) {
			responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			
		} else if(ErrorCode.QECx02.equals(stockPriceException.getErrorCode())) {
			responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
			
		} else if(ErrorCode.QESx04.equals(stockPriceException.getErrorCode())) {
			responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
			
		} else if(ErrorCode.QWARN01.equals(stockPriceException.getErrorCode())) {
			responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
			
		} else {
			// for unknown ErrorCode, set default http status to 404
			responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}
		
		return responseEntity;
	}
	
}	
