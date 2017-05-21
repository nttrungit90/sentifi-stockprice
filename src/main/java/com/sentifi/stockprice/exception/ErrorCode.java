package com.sentifi.stockprice.exception;

public enum ErrorCode {
	// default error code QEXx03
	QEXx03("QEXx03", "Something went wrong. Please try again. If you continue to have problems, please contact us at connect@quandl.com."),
	QECx02("QECx02", "You have submitted an incorrect Quandl code. Please check your Quandl codes and try again."),
	QESx04("QESx04", "You have submitted incorrect query parameters. Please check your API call syntax and try again."),
	
	QWARN01("QWARN01", "No data found for your request. Please try with other value of request parameters.");

	private final String code;
	private final String description;

	private ErrorCode(String code, String description) {
	    this.code = code;
	    this.description = description;
	  }

	public String getDescription() {
		return description;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + description;
	}

}
