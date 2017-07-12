package com.softbistro.orderbooks.components.entity;

import java.util.List;

public class CatalogItem {
	private String catalogItemId;
	private String name;
	private List<PriceItem> prices;

	public CatalogItem() {

	}

	public CatalogItem(String catalogItemId, String name, List<PriceItem> prices) {
		this.catalogItemId = catalogItemId;
		this.name = name;
		this.prices = prices;
	}

	public String getCatalogItemId() {
		return catalogItemId;
	}

	public void setCatalogItemId(String catalogItemId) {
		this.catalogItemId = catalogItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PriceItem> getPrices() {
		return prices;
	}

	public void setPrices(List<PriceItem> prices) {
		this.prices = prices;
	}

}
