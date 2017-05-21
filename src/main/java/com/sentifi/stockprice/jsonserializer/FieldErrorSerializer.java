package com.sentifi.stockprice.jsonserializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class FieldErrorSerializer extends StdSerializer<Map<String, List<String>>> {
	private static final long serialVersionUID = -4475966347467196140L;
	public FieldErrorSerializer() { 
        this(null); 
    } 
 
    public FieldErrorSerializer(Class<Map<String, List<String>>> t) {
        super(t); 
    }
 
    @Override
    public void serialize(
    		Map<String, List<String>> errorMap, JsonGenerator jGenerator, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
    	// sample json
    	/*
    	{
	        "start_date": ["start_date should not exceed end_date", "other error message for this start_date field"]
	        "end_date": ["end_date should not less than year 2000"]
    	}
        */
    	
    	// start write the map to object
    	jGenerator.writeStartObject();
    	
    	if(errorMap != null && !errorMap.isEmpty()) {
    		for(String fieldName : errorMap.keySet()) {
    			// write ticker field
    			jGenerator.writeArrayFieldStart(fieldName);
    			for(String errorMessage : errorMap.get(fieldName)) {
    				jGenerator.writeString(errorMessage);
    			}
    			jGenerator.writeEndArray();
        	}
    	}
    	// end write object
    	jGenerator.writeEndObject();
    }
}