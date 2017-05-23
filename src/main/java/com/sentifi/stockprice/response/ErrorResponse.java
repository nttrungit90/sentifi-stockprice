package com.sentifi.stockprice.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sentifi.stockprice.jsonserializer.FieldErrorSerializer;

import io.swagger.annotations.ApiModelProperty;

public class ErrorResponse {

	@ApiModelProperty(notes = "Hold value of the invalid field")
	@JsonSerialize(using = FieldErrorSerializer.class)
	private Map<String, List<String>> errors = new HashMap<String, List<String>>();
	
	@ApiModelProperty(notes = "Hold error code and corresponding message")
	private StockPriceError stockPriceError;
	
	public ErrorResponse(StockPriceError stockPriceError, Map<String, List<String>> errors) {
		super();
		this.stockPriceError = stockPriceError;
		this.errors = errors;
	}
	
	public ErrorResponse(StockPriceError stockPriceError) {
		super();
		this.stockPriceError = stockPriceError;
	}
	
	public StockPriceError getStockPriceError() {
		return stockPriceError;
	}
	public void setStockPriceError(StockPriceError stockPriceError) {
		this.stockPriceError = stockPriceError;
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
