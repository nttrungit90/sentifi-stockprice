package com.sentifi.stockprice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StockPriceDateUtil {
	/**
	 * Validate the date with given format
	 * @param dateToValidate
	 * @param dateFromat
	 * @return true if valid
	 */
	public static boolean isThisDateValid(String dateToValidate, String dateFormat){

		if(dateToValidate == null || dateToValidate.isEmpty()){
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		try {
			//if not valid, it will throw ParseException
			sdf.parse(dateToValidate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	/**
	 * Get a new date from the current date with plus daysToIncrease
	 * @param date
	 * @param daysToIncrease
	 * @param dateFormat
	 * @return a new date from the current date with plus daysToIncrease
	 * @throws ParseException 
	 */
	public static String increaseDate(String date, int daysToIncrease, String dateFormat) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		//if not valid, it will throw ParseException
		Date startDateObject = sdf.parse(date);
		
		// convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(startDateObject);

        // manipulate date
        c.add(Calendar.DATE, daysToIncrease);

        // convert calendar to date
        Date currentDatePlus = c.getTime();

        return sdf.format(currentDatePlus);
	        
	}
}
