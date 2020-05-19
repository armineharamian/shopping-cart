package com.ethocatests.catalogservice.store.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.ethocatests.catalogservice.store.Cart;
import com.ethocatests.catalogservice.store.data.CartItem;

public class InMemoryCart implements Cart {

	private ConcurrentHashMap<String, CartItem> items = new ConcurrentHashMap<>();

	@Override
	public List<CartItem> items() {
		return items.values().stream().sorted((ci1, ci2) -> 
			Long.compare(ci1.getTimestamp(), ci2.getTimestamp())
		).collect(Collectors.toList());
	}

	public void remove(String itemId) {
		items.remove(itemId);
	}

	public CartItem get(String itemId) {
		CartItem item = items.get(itemId);
		if(item == null) {
			throw new IllegalArgumentException("unknown item id " + itemId);
		}
		return item;
	}

	public boolean add(CartItem item) {
		CartItem previous = items.putIfAbsent(item.getId(), item);
		return previous == null;
	}
}
