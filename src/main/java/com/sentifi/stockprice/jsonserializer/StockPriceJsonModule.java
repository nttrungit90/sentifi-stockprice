package com.sentifi.stockprice.jsonserializer;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
/**
 * This class is used to register json serializer class for object model
 * @author admin
 *
 */
public class StockPriceJsonModule extends SimpleModule {
	private static final long serialVersionUID = -6005705811169215177L;
	private static final String NAME = "TickerModule";
	private static final VersionUtil VERSION_UTIL = new VersionUtil() {
	};

	/**
	 * Add 
	 */
	public StockPriceJsonModule() {
		super(NAME, VERSION_UTIL.version());
	}
}