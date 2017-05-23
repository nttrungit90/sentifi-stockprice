package com.sentifi.stockprice.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockPriceException extends Exception {
	private static final long serialVersionUID = 1432302583113741772L;
	
	private ErrorCode errorCode;
	// to store error messages of request parameters
	private Map<String, List<String>> errors = new HashMap<String, List<String>>();
	
	public StockPriceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
	
	public StockPriceException(ErrorCode code, Map<String, List<String>> errors) {
        this.errorCode = code;
        this.errors = errors;
    }
	
	public StockPriceException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}


	public StockPriceException(String message, ErrorCode errorCode, Map<String, List<String>> errors) {
		super(message);
		this.errorCode = errorCode;
		this.errors = errors;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public Map<String, List<String>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, List<String>> errors) {
		this.errors = errors;
	}
	
	public void addErrors(String key, List<String> errors) {
		if(this.errors.get(key) == null) {
			this.errors.put(key, errors);
		} else {
			this.errors.get(key).addAll(errors);
		}
	}
	
	public void addErrors(String key, String error) {
		if(this.errors.get(key) == null) {
			List<String> list =  new ArrayList<String>();
			list.add(error);
			this.errors.put(key, list);
		} else {
			this.errors.get(key).add(error);
		}
	}

}
