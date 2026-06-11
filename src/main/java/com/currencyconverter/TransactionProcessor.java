package com.currencyconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionProcessor {
	
	public static void main (String[] args)
	{
		Converter converter = new Converter();
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		
		String transactionFileName = "src/main/resources/transactions.txt";
		
		transactionProcessor.readTransactionsFromFile(transactionFileName);
				
		for (Transactions transaction : transactionList)
		{
										
			converter.convert(transaction.getCurrencyFrom(),transaction.getCurrencyTo(), transaction.getAmount());
			transactionProcessor.executeTransaction(transaction.getName());
			transactionProcessor.updateUsersFile();
			
		}
	}

	private static ObjectMapper reader = new ObjectMapper();
	private static ObjectMapper writer = new ObjectMapper();
	private static Logger logger = LogManager.getLogger(TransactionProcessor.class);
	public static User[] user;
	private static List<Transactions> transactionList = new ArrayList<>();
	Double updatedCurrencyFrom;
	Double updatedCurrencyTo;
	
	public void executeTransaction(String name)
	{			
		try
		{
		    File jsonFile = new File("src/main/resources/users.json");  
	
		    user = reader.readValue(jsonFile, User[].class);		    
		}
		
		catch (FileNotFoundException fnfe)
		{
			logger.error("A FileNotFoundException was thrown.");
			fnfe.printStackTrace();
		}
		
		catch (IOException e)
		{
			logger.error("An IOException was thrown.");
			e.printStackTrace();
		}
		
		boolean exists = false;
		
		for(User username : user)
		{
			if(username.getName().equals(name))
			{
				exists = true;
			}
			
		}
		
		if (!exists)
			{
				logger.error("Transaction skipped - This user does not exist.");
				return;
			}
		

		int programChecks = 0;
		
		for (Map.Entry<String, Currency> entry : Converter.fxRates.entrySet())
		{
			
			if (Converter.currencyFrom.equals(entry.getKey()))
			{
				programChecks++;
			}
			
			if (Converter.currencyTo.equals(entry.getKey()))
			{
				programChecks++;
			}
			
		}
		
		if(Converter.currencyFrom.equals("usd"))
		{
			programChecks++;
		}
		
		if(Converter.currencyTo.equals("usd"))
		{
			programChecks++;
		}
		
		if (programChecks != 2)
		{
			logger.error("Transaction skipped - This currency does not exist.");
			return;
		}
		
		if (Converter.currencyTo.equals(Converter.currencyFrom))
		{
	        logger.info("Transaction skipped - The currencies are the same.");
			return;
		}
		
		
		for (User userInfo : user)
		{
			if(userInfo.getName().equals(name))
			{
				
				for (Map.Entry<String, Double> entry : userInfo.getWallet().entrySet())
				{
					if (Converter.currencyFrom.equals(entry.getKey()))
					{
						programChecks++;
					}
				}
				
				if (programChecks != 3)
				{
			        logger.warn("Transaction skipped - There is no such currency in the wallet for conversion.");
					return;
				}
				
			
				if (Converter.amountToConvert > userInfo.getWallet().get(Converter.currencyFrom))
				{	
			        logger.info("Transaction skipped - There is insufficient currency for transfer.");
					return;
				}
				
			
				updatedCurrencyFrom = userInfo.getWallet().get(Converter.currencyFrom) - Converter.amountToConvert;
				
				for (Map.Entry<String, Double> entry : userInfo.getWallet().entrySet())
				{
					if (entry.getKey().equals(Converter.currencyFrom))
					{
						entry.setValue(updatedCurrencyFrom);
					}
				}
						
				if(!userInfo.getWallet().containsKey(Converter.currencyTo))
				{
					userInfo.getWallet().put(Converter.currencyTo, 0.00);			
				}
				
				updatedCurrencyTo = userInfo.getWallet().get(Converter.currencyTo) + Converter.convertedAmount;
				
				for (Map.Entry<String, Double> entry : userInfo.getWallet().entrySet())
				{
					if (entry.getKey().equals(Converter.currencyTo))
					{
						entry.setValue(updatedCurrencyTo);
					}
										
				}
			}
		}
		logger.trace("Currency was successfully converted.");
	}
	
	public void updateUsersFile()
	{
		try
		{
			File destination = new File("src/main/resources/users.json");
			writer.writeValue(destination,user);
		}
		
		catch (IOException e)
		{
			logger.error("An IOException was thrown.");
			e.printStackTrace();
		}
	}	
	
	public void readTransactionsFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
           
        	String line;
        	
            while ((line = br.readLine()) != null) {
                String[] element = line.split(" ");
                String name = element[0];
                String currencyFrom = element[1];
                String currencyTo = element[2];
                double amount = Integer.parseInt(element[3]);
               
                Transactions transactions = new Transactions();
                transactions.setName(name);
                transactions.setCurrencyFrom(currencyFrom);
                transactions.setCurrencyTo(currencyTo);
                transactions.setAmount(amount);
                
                transactionList.add(transactions);
          
            }
        } 
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
