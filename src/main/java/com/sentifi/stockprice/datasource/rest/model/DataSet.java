package com.sentifi.stockprice.datasource.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSet {
	public static final String COLUMN_DATE = "Date";
	public static final String COLUMN_OPEN = "Open";
	public static final String COLUMN_HIGH = "High";
	public static final String COLUMN_LOW = "Low";
	public static final String COLUMN_CLOSE = "Close";
	public static final String COLUMN_VOLUME = "Volume";
	public static final String COLUMN_EX_DIVIDEND = "Ex-Dividend";
	public static final String COLUMN_ADJ_OPEN= "Adj. Open";
	public static final String COLUMN_ADJ_HIGH= "Adj. High";
	public static final String COLUMN_ADJ_LOW = "Adj. Low";
	public static final String COLUMN_ADJ_CLOSE = "Adj. Close";
	public static final String COLUMN_ADJ_VOLUME = "Adj. Volume";
	
	private long id;
	private String datasetCode;
	private String newestAvailableDate;
	private String oldestAvailableDate;
	private String startDate;
	private String endDate;
	private List<String> columnNames = new ArrayList<String>();
	private List<List<Object>> data = new ArrayList<List<Object>>();
	
	public DataSet() {
		super();
	}
	public long getId() {
		return id;
	}
	@JsonProperty("id")
	public void setId(long id) {
		this.id = id;
	}
	public String getDatasetCode() {
		return datasetCode;
	}
	// because the returned json has different field name with our model object, 
	// so we need to use @JsonProperty to do mapping
	@JsonProperty("dataset_code")
	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}
	public String getStartDate() {
		return startDate;
	}
	@JsonProperty("start_date")
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	@JsonProperty("end_date")
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<List<Object>> getData() {
		return data;
	}
	@JsonProperty("data")
	public void setData(List<List<Object>> data) {
		this.data = data;
	}
	public String getNewestAvailableDate() {
		return newestAvailableDate;
	}
	@JsonProperty("newest_available_date")
	public void setNewestAvailableDate(String newestAvailableDate) {
		this.newestAvailableDate = newestAvailableDate;
	}
	public String getOldestAvailableDate() {
		return oldestAvailableDate;
	}
	@JsonProperty("oldest_available_date")
	public void setOldestAvailableDate(String oldestAvailableDate) {
		this.oldestAvailableDate = oldestAvailableDate;
	}
	public List<String> getColumnNames() {
		return columnNames;
	}
	@JsonProperty("column_names")
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}
	
	
}
