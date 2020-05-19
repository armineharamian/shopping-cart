package com.ethocatests.catalogservice.store.impl;

import java.util.concurrent.atomic.AtomicInteger;

import com.ethocatests.catalogservice.store.StockItem;
import com.ethocatests.catalogservice.store.data.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StockItemImpl implements StockItem {
	private Item item;
	private String id;
	private AtomicInteger quantity;
	
	public StockItemImpl(String id, Item item, int quantity) {
		this.id = id;
		this.item = item;
		this.quantity = new AtomicInteger(quantity);
	}

	@JsonProperty("item")
	@Override
	public Item getItem() {
		return item;
	}

	@JsonProperty("id")
	@Override
	public String getId() {
		return id;
	}

	@JsonProperty("available_quantity")
	@Override
	public int availableQuantity() {
		return quantity.get();
	}
	
	@Override
	public boolean subtractQuantity(int q) {
		int current = quantity.get();
		if(current < q) {
			return false;
		}
		return quantity.compareAndSet(current, current - q);
	}
}
