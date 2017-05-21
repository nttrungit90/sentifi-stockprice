package com.sentifi.stockprice.jsonserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class QuandLFieldErrorDeserializer extends JsonDeserializer<Map<String, List<String>>>{

	@Override
	public Map<String, List<String>> deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		// deserialize this json to Map<String, List<String>> 
		/*
		  {
        	"start_date": ["start_date should not exceed end_date"],
        	"end_date": ["end_date should equal or exceed start_date"]
    	  }
		 */
		
		Map<String, List<String>> errorsMap = new HashMap<String, List<String>>();
		
		JsonNode node = jp.getCodec().readTree(jp);
		Iterator<Entry<String, JsonNode>> iterator = node.fields();
		Entry<String, JsonNode> entry = null;
		List<String> errors =  null;
		String key = "";
		JsonNode jsonNode = null;
		while(iterator.hasNext()) {
			entry = iterator.next();
			key = entry.getKey();
			errors = new ArrayList<String>();
			
			jsonNode = entry.getValue();
			if (jsonNode.isArray()) {
			    for (final JsonNode objNode : jsonNode) {
			    	errors.add(objNode.textValue());
			    }
			}
			errorsMap.put(key, errors);
		}
		return errorsMap;
	}

}
