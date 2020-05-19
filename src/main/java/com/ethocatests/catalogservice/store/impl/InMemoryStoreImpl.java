package com.ethocatests.catalogservice.store.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ethocatests.catalogservice.store.Cart;
import com.ethocatests.catalogservice.store.Carts;
import com.ethocatests.catalogservice.store.StockItem;
import com.ethocatests.catalogservice.store.Store;
import com.ethocatests.catalogservice.store.data.CartItem;
import com.ethocatests.catalogservice.store.data.Item;

@Service
@Scope(value = "singleton")
public class InMemoryStoreImpl implements Store {
	private Map<String, StockItemImpl> stockItems;
	
	@Autowired
	private Carts carts;
	
	
	public InMemoryStoreImpl() {
		stockItems = new LinkedHashMap<>();
		stockItems.put("good1", new StockItemImpl("good1", 
				             new Item("American Girl Wellywisher Doll", 
				                      "American Girl Wellywisher Doll, an iconic toy for many generations, with a new fresh look.",
				                      new BigDecimal("59.99")),
				             3));
				
		stockItems.put("good2", new StockItemImpl("good2", 
	                         new Item("Wall-e Singing Robot", 
	                                  "Sing along, dance and play with this awesome toy.",
	                                  new BigDecimal("98.99")),
	                         2));
	}
	
	
	@Override
	public List<? extends StockItem> catalog() {
		return stockItems.values().stream().collect(Collectors.toList());
	}

	@Override
	public boolean addToCart(String userId, String itemId, int quantity) {
		StockItem item = stockItems.get(itemId);
		if(item == null) {
			throw new IllegalArgumentException("Unknown item id " + itemId);
		}
		if(item.availableQuantity() >= quantity) {
			CartItem cartItem = new CartItem(item, quantity, System.currentTimeMillis());
			return carts.add(userId, cartItem);
		} else {
			return false;
		}
	}

	@Override
	public void checkout(String userId) {
		
		Cart cart = carts.get(userId);
		for(CartItem item : cart.items()) {
			StockItemImpl stockItem = stockItems.get(item.getId());
			// We are sure stockItem is not null here
			// since our store is currently read-only i.e. no goods can be deleted
			// from the catalog
			int requestedQuantity = item.getCartQuantity();
			if(!stockItem.subtractQuantity(requestedQuantity)) {
				throw new IllegalStateException(item.getId() + 
						          " a.q. is less than " + requestedQuantity);
			}
		}
		// Here is the last step of our checkout process.
		// Normally synchronization is needed to make sure we
		// are not charging the user twice on, but since we don't have
		// a payment mechanism, we are simply removing the cart, without worrying
		// if the cart has already been checked out (removed) by another parallel
		// request.
		carts.remove(userId);
	}

	@Override
	public Cart cart(String userId) {
		return carts.get(userId);
	}
	
	
}
