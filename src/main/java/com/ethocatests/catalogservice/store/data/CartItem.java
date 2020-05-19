package com.ethocatests.catalogservice.store.data;

import java.math.BigDecimal;

import com.ethocatests.catalogservice.store.StockItem;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItem {
	private StockItem item;
	private int cartQuantity;
	private long timestamp;
	
	public CartItem(StockItem item, int cartQuantity, long timestamp) {
		this.item = item;
		this.cartQuantity = cartQuantity;
		this.timestamp = timestamp;
	}
	
	@JsonProperty("id")
	public String getId() {
		return item.getId();
	}
	
	@JsonProperty("title")
	public String getTitle() {
		return item.getItem().getTitle();
	}
	
	@JsonProperty("price")
	public BigDecimal getPrice() {
		return item.getItem().getPrice();
	}
	
	@JsonProperty("available_quantity")
	public int getAvailableQuantity() {
		return item.availableQuantity();
	}
	
	@JsonProperty("cart_quantity")
	public int getCartQuantity() {
		return this.cartQuantity;
	}
	
	public void setCartQuantity(int quantity) {
		cartQuantity = quantity;
		timestamp = System.currentTimeMillis();
	}
	
	@JsonProperty("timestamp")
	public long getTimestamp() {
		return this.timestamp;
	}
}
