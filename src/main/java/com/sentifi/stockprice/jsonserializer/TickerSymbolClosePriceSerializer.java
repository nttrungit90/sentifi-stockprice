package com.sentifi.stockprice.jsonserializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sentifi.stockprice.vo.ClosePrice;
import com.sentifi.stockprice.vo.TickerSymbolClosePrice;

public class TickerSymbolClosePriceSerializer extends StdSerializer<TickerSymbolClosePrice> {
	private static final long serialVersionUID = -4475966347467196040L;
	private static final String TICKER_FIELD = "Ticker";
	private static final String DATE_CLOSE_FIELD = "DateClose";

	public TickerSymbolClosePriceSerializer() { 
        this(null); 
    } 
 
    public TickerSymbolClosePriceSerializer(Class<TickerSymbolClosePrice> t) {
        super(t); 
    }
 
    @Override
    public void serialize(
    		TickerSymbolClosePrice tickerSymbolClosePrice, JsonGenerator jGenerator, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
    	// sample json
    	/*
    	{
            "Ticker": "GE",
            "DateClose": ["1999-03-30", "28.32"],
            "DateClose": ["1999-03-30", "28.32"]
        }
        */
    	
    	// start write TickerSymbolClosePrice json 
    	jGenerator.writeStartObject();
    	
    	// write ticker field
    	jGenerator.writeFieldName(TICKER_FIELD);
    	jGenerator.writeString(tickerSymbolClosePrice.getTicker());
    	
    	// for each 'DateClose' and write each as an array field with them same key DATE_CLOSE_FIELD
    	List<ClosePrice> dateCloses = tickerSymbolClosePrice.getCloseDates();
    	if(dateCloses.isEmpty()) {
    		jGenerator.writeArrayFieldStart(DATE_CLOSE_FIELD);// "DateClose" :[
    		jGenerator.writeEndArray(); // ]
    	} else {
    		for(ClosePrice dateClose : dateCloses) {
        		jGenerator.writeArrayFieldStart(DATE_CLOSE_FIELD);// "DateClose" :[
            	jGenerator.writeString(dateClose.getDate());
            	String close = dateClose.getClose() != null ? dateClose.getClose().toString(): "";
            	jGenerator.writeString(close);
            	jGenerator.writeEndArray(); // ]
        	}
    	}
    	
    	// end write TickerSymbolClosePrice
    	jGenerator.writeEndObject();
    }
}