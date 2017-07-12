package com.softbistro.orderbooks.components.entity;

public class PriceItem {

	private Double price;
	private String logId;

	public PriceItem() {

	}

	public PriceItem(Double price, String logId) {
		this.price = price;
		this.logId = logId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

}
