package com.ethocatests.catalogservice.store.data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("price")
	private BigDecimal price;
	
	public Item(String title, String description, BigDecimal price) {
		this.title = title;
		this.description = description;
		this.price = price;
	}


	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
}
