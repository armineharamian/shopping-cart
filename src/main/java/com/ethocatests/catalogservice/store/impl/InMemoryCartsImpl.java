package com.ethocatests.catalogservice.store.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ethocatests.catalogservice.store.Cart;
import com.ethocatests.catalogservice.store.Carts;
import com.ethocatests.catalogservice.store.data.CartItem;

@Service
@Scope(value = "singleton")
public class InMemoryCartsImpl implements Carts {
	
	private ConcurrentHashMap<String, InMemoryCart> carts = new ConcurrentHashMap<>();

	private InMemoryCart cart(String userId) {
		InMemoryCart cart = carts.get(userId);
		if(cart == null) {
			throw new IllegalArgumentException("Unknown user id " + userId);
		}
		return cart;
	}
	
	@Override
	public void removeFromCart(String userId, String itemId) {
		cart(userId).remove(itemId);
	}

	@Override
	public void setQuantity(String userId, String itemId, int quantity) {
		CartItem item = cart(userId).get(itemId);
		if(quantity > item.getAvailableQuantity()) {
			throw new IllegalArgumentException(quantity + " is not available for " + itemId);
		}
		item.setCartQuantity(quantity);
	}

	@Override
	public boolean add(String userId, CartItem item) {
		InMemoryCart cart = carts.get(userId);
		if(cart == null) {
			cart = new InMemoryCart();
		}
		InMemoryCart previous = carts.putIfAbsent(userId, cart);
		if(previous != null) {
			cart = previous;
		}
		return cart.add(item);
	}

	@Override
	public Cart get(String userId) {
		return cart(userId);
	}
	
	@Override
	public void remove(String userId) {
		carts.remove(userId);
	}

}
