package com.softbistro.orderbooks.components.entity;

public class BookForOrder {
	private String catalogItemId;
	private String pricingId;
	private Integer orderId;
	private Integer quantity;
	private String shippingChoiceHash;

	public BookForOrder(String catalogItemId, String pricingId) {
		this.catalogItemId = catalogItemId;
		this.pricingId = pricingId;
	}

	public BookForOrder(Integer orderId, String shippingChoiceHash) {
		this.orderId = orderId;
		this.shippingChoiceHash = shippingChoiceHash;
	}

	public BookForOrder(String catalogItemId, String pricingId, Integer orderId, Integer quantity) {
		this.catalogItemId = catalogItemId;
		this.pricingId = pricingId;
		this.orderId = orderId;
		this.quantity = quantity;
	}

	public BookForOrder(String catalogItemId, String pricingId, Integer orderId, Integer quantity,
			String shippingChoiceHash) {
		this.catalogItemId = catalogItemId;
		this.pricingId = pricingId;
		this.orderId = orderId;
		this.quantity = quantity;
		this.shippingChoiceHash = shippingChoiceHash;
	}

	public BookForOrder(String catalogItemId) {
		this.catalogItemId = catalogItemId;
	}

	public BookForOrder(Integer orderId) {
		this.orderId = orderId;
	}

	public String getShippingChoiceHash() {
		return shippingChoiceHash;
	}

	public void setShippingChoiceHash(String shippingChoiceHash) {
		this.shippingChoiceHash = shippingChoiceHash;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public BookForOrder() {
	}

	public String getCatalogItemId() {
		return catalogItemId;
	}

	public void setCatalogItemId(String catalogItemId) {
		this.catalogItemId = catalogItemId;
	}

	public String getPricingId() {
		return pricingId;
	}

	public void setPricingId(String pricingId) {
		this.pricingId = pricingId;
	}
}
