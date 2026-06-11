package com.fdm.currencyconverter;

import java.util.Map;

public class User {

	private String name;
	public Map<String, Double> wallet;
	
	public User(String name, Map<String, Double> wallet)
	{	
		super();
		this.name = name;
		this.wallet = wallet;		
	}
	
	public User()
	{		
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Double> getWallet() {
		return wallet;
	}

	public void setWallet(Map<String, Double> wallet) {
		this.wallet = wallet;
	}
}