package com.fdm.currencyconverter;

public class Transactions {

	private String name;
	private String currencyTo;
	private String currencyFrom;
	private double amount;
	
	public Transactions() {
		super();
	}	

	public Transactions(String name, String currencyTo, String currencyFrom, double amount) {
		super();
		this.name = name;
		this.currencyTo = currencyTo;
		this.currencyFrom = currencyFrom;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrencyTo() {
		return currencyTo;
	}

	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
