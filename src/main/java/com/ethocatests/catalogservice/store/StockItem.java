package com.ethocatests.catalogservice.store;

import com.ethocatests.catalogservice.store.data.Item;

public interface StockItem {
	String getId();
	
	Item getItem();
	
	int availableQuantity();
	
	/**
	 * Subtract available quantity by {@code q} and return {@code true} if 
	 * the operation was successful, {@code false} otherwise.
	 * Implementations must guarantee available quantity never becomes negative.
	 * @param q quantity to subtract from available quantity.
	 * @return
	 */
	boolean subtractQuantity(int q);
}
