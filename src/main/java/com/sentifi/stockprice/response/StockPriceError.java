package com.sentifi.stockprice.response;

public class StockPriceError {

	private String code;
	private String message;

	public StockPriceError() {
	}
	
	public StockPriceError(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
