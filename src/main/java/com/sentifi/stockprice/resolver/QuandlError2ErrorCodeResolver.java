package com.sentifi.stockprice.resolver;

import java.util.Arrays;
import java.util.List;

import com.sentifi.stockprice.datasource.rest.model.QuandlError;
import com.sentifi.stockprice.exception.ErrorCode;

public class QuandlError2ErrorCodeResolver {
	
	public static ErrorCode resolve(QuandlError quandlError) {
		List<ErrorCode> errorCodes = Arrays.asList(ErrorCode.values());
		// return default error code if no match error code found
		ErrorCode retErrorCode = ErrorCode.QEXx03;
		for(ErrorCode code : errorCodes) {
			if(code.getCode().equalsIgnoreCase(quandlError.getCode())) {
				retErrorCode = code;
				break;
			}
		}
		
		return retErrorCode;
	}
}
