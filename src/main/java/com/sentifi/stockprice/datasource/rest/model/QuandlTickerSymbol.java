package com.sentifi.stockprice.datasource.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuandlTickerSymbol {
	
	private DataSet dataSet;
	
	public QuandlTickerSymbol() {
		super();
	}

	public QuandlTickerSymbol(DataSet dataSet) {
		super();
		this.dataSet = dataSet;
	}

	@JsonProperty("dataset")
	public DataSet getDataSet() {
		return dataSet;
	}

	@JsonProperty("dataset")
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
}
