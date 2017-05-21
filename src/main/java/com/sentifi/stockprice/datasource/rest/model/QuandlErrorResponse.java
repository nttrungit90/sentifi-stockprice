package com.sentifi.stockprice.datasource.rest.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sentifi.stockprice.jsonserializer.QuandLFieldErrorDeserializer;

public class QuandlErrorResponse {

	@JsonDeserialize(using = QuandLFieldErrorDeserializer.class)
	private Map<String, List<String>> errors = new HashMap<String, List<String>>();
	
	private QuandlError quandlError;

	public QuandlErrorResponse() {
		super();
	}
	
	public QuandlErrorResponse(Map<String, List<String>> errors, QuandlError quandlError) {
		super();
		this.errors = errors;
		this.quandlError = quandlError;
	}

	public Map<String, List<String>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, List<String>> errors) {
		this.errors = errors;
	}

	@JsonProperty("quandl_error")
	public QuandlError getQuandlError() {
		return quandlError;
	}

	public void setQuandlError(QuandlError quandlError) {
		this.quandlError = quandlError;
	}

}
