package com.fdm.currencyconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {

	public static String currencyFrom;	
	public static String currencyTo;
	public static Double amountToConvert;
	public static Double convertedAmount;
	public static Double rateTo;
	public static Double rateFrom;
	public static Map<String, Currency> fxRates;
	private static ObjectMapper mapper = new ObjectMapper();	
	   
	public double convert(String from, String to, double amount)
	{
		try
		{
		    File jsonFile = new File("src/main/resources/fx_rates.json");  
	
		    fxRates = mapper.readValue(jsonFile, new TypeReference<Map<String, Currency>>(){});		    
		}
		
		catch (FileNotFoundException fnfe)
		{
			System.out.println("File not found.");
			fnfe.printStackTrace();
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		rateTo = 0.00;
		rateFrom = 0.00;
		currencyFrom = from.toLowerCase();
		currencyTo = to.toLowerCase();
		amountToConvert = amount;
		
    	
	    for (Map.Entry<String,Currency> entry : fxRates.entrySet())
	    {	
	        if(entry.getKey().equals(currencyFrom))
	        {
	        	rateFrom = entry.getValue().getRate(); 	
	        }
	        
	        if(entry.getKey().equals(currencyTo))
	        {
	        	rateTo = entry.getValue().getRate();
	        }
	        
	        if(currencyFrom.equals("usd"))
	        {
	        	rateFrom = 1.00; 	
	        }
	        
	        if(currencyTo.equals("usd"))
	        {
	        	rateTo = 1.00; 	
	        }
	    }
		
		convertedAmount = amountToConvert / rateFrom * rateTo;
		
		return convertedAmount;
	}
}
