package com.ethocatests.catalogservice.store;

import java.util.List;

public interface Store {
	
	List<? extends StockItem> catalog();
	
	boolean addToCart(String userId, String itemId, int quantity);
	
	Cart cart(String userId);
	
	/**
	 * Checkout current cart items for given {@code userId}.
	 * @param userId user identifier.
	 * @throws IllegalStateException if the available quantity for one or more items
	 *                               has changed between adding to cart and checking out
	 *                               and the user requested quantities cannot be fulfilled.  
	 */
	void checkout(String userId);
}
