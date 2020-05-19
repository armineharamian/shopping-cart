package com.ethocatests.catalogservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ethocatests.catalogservice.store.Cart;
import com.ethocatests.catalogservice.store.Carts;
import com.ethocatests.catalogservice.store.StockItem;
import com.ethocatests.catalogservice.store.Store;

@RestController
public class CatalogServiceController {
	@Autowired
	private Store store;
	
	@Autowired
	private Carts carts;
	
	Logger logger = LoggerFactory.getLogger("ItemCatalogResource");
	
	@GetMapping(path = "/catalog", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<? extends StockItem> getCatalog(){
		return store.catalog();
	}

	// PUT /users/{userId}/cart/{itemId}?quantity={quantity}
	@PutMapping(path = "/users/{user_id}/cart/{item_id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addItemToCart(@PathVariable("user_id") String userId, 
									          @PathVariable("item_id") String itemId, 
									          @RequestParam int quantity) {
		try {
			boolean successful = store.addToCart(userId, itemId, quantity);
			if(!successful) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch(IllegalArgumentException iae) {
			logger.warn("Invalid item id {}, cannot add to {}", itemId, userId);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// GET /users/{userId}/cart
	@GetMapping(path = "/users/{user_id}/cart", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cart> getUserCart(@PathVariable("user_id") String userId) {
		try { 
			return new ResponseEntity<>(carts.get(userId), HttpStatus.OK);	
		} catch(IllegalArgumentException iae) {
			logger.warn("Invalid user id {}. No cart found for the user.",  userId);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// DELETE /users/{userId}/cart/{itemId}
	@DeleteMapping(path = "/users/{user_id}/cart/{item_id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteCartItem(@PathVariable("user_id") String userId, 
			                                   @PathVariable("item_id") String itemId) {
		try {
			carts.removeFromCart(userId, itemId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(IllegalArgumentException iae) {
			logger.warn("Invalid item {} for user {}. Delete operation failed.", itemId, userId);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	// POST /users/{userId}/cart/{itemId}?quantity={quantity}
	@PostMapping(path = "/users/{user_id}/cart/{item_id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateCartItemQuantity(@PathVariable("user_id") String userId, 
									          @PathVariable("item_id") String itemId, 
									          @RequestParam int quantity) {
		try {
			carts.setQuantity(userId, itemId, quantity);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(IllegalArgumentException iae) {
			logger.warn("Invalid quantity {} for cart item {}. {}", quantity, itemId, iae.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	// POST /users/{userId}/_checkout
	@PostMapping(path="/users/{user_id}/_checkout")
	public ResponseEntity<Void> checkoutCart(@PathVariable("user_id") String userId){
		try {
			store.checkout(userId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(IllegalArgumentException iae) {
			logger.warn("Cart checkout failed for user {}: {}", userId, iae.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(IllegalStateException ise) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}
}
