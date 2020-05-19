package com.ethocatests.catalogservice.store;

import java.util.List;

import com.ethocatests.catalogservice.store.data.CartItem;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface Cart {
	enum Order {
		LAST_MODIFIED_ASC, LAST_MODIFIED_DESC;
	}
	
	Order DEFAULT_ORDER = Order.LAST_MODIFIED_ASC;
	
	@JsonProperty("items")
	List<CartItem> items();
}
