package com.ethocatests.catalogservice.store;

import com.ethocatests.catalogservice.store.data.CartItem;

public interface Carts {
	
	void removeFromCart(String userId, String itemId);
	
	void setQuantity(String userId, String itemId, int quantity);
	
	boolean add(String userId, CartItem item);
	
	Cart get(String userId);
	
	void remove(String userId);
}
