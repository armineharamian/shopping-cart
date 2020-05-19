package com.ethocatests.catalogservice;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ethocatests.catalogservice.store.StockItem;
import com.ethocatests.catalogservice.store.data.CartItem;
import com.ethocatests.catalogservice.store.data.Item;
import com.ethocatests.catalogservice.store.impl.InMemoryCart;
import com.ethocatests.catalogservice.store.impl.InMemoryCartsImpl;
import com.ethocatests.catalogservice.store.impl.StockItemImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InMemoryCartsImplTest {
	@Autowired
	InMemoryCartsImpl carts;
	
	
	@Test
	void testUserCartDoesNotExist() {
		//no cart exists for the user id "abc"
		//throws IllegalargumentException
		 Assertions.assertThrows(IllegalArgumentException.class, () -> {
			 carts.get("abc");
		 });
	}
	
	@Test
	void tetsAddItemToCart() {
		StockItem stockItem1 = new StockItemImpl("si1", new Item("Item1", "Item1_desc", new BigDecimal("59.99")), 3);
		StockItem stockItem2 = new StockItemImpl("si2", new Item("Item2", "Item2_desc", new BigDecimal("59.99")), 2);
		
		CartItem cartItem1 = new CartItem(stockItem1, 3, System.currentTimeMillis());
		
		CartItem cartItem2 = new CartItem(stockItem2, 2, System.currentTimeMillis());
		
		carts.add("user1", cartItem1);
		carts.add("user1", cartItem2);
		
		//shouldn't fail with exception
		assertNotNull(carts.get("user1"));
		//cart has 2 items added
		assertEquals(2, carts.get("user1").items().size());
	}
	
	@Test
	void testRemoveFromCart() {
		StockItem stockItem1 = new StockItemImpl("si1", new Item("Item1", "Item1_desc", new BigDecimal("59.99")), 3);
		StockItem stockItem2 = new StockItemImpl("si2", new Item("Item2", "Item2_desc", new BigDecimal("59.99")), 2);
		
		CartItem cartItem1 = new CartItem(stockItem1, 3, System.currentTimeMillis());
		CartItem cartItem2 = new CartItem(stockItem2, 2, System.currentTimeMillis());
		
		carts.add("user1", cartItem1);
		carts.add("user1", cartItem2);
		
		carts.removeFromCart("user1", "si1");
		
		assertEquals(1, carts.get("user1").items().size());
		
	}
	
	@Test
	void testRemoveCart() {
		StockItem stockItem1 = new StockItemImpl("si1", new Item("Item1", "Item1_desc", new BigDecimal("59.99")), 3);
		StockItem stockItem2 = new StockItemImpl("si2", new Item("Item2", "Item2_desc", new BigDecimal("59.99")), 2);
		
		CartItem cartItem1 = new CartItem(stockItem1, 3, System.currentTimeMillis());
		CartItem cartItem2 = new CartItem(stockItem2, 2, System.currentTimeMillis());
		
		carts.add("user1", cartItem1);
		carts.add("user1", cartItem2);
		
		carts.remove("user1");
		
		//cart not found
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			 carts.get("user1");
		 });
		
	}
	
	@Test
	void testSetQuantity() {
		StockItem stockItem1 = new StockItemImpl("si1", new Item("Item1", "Item1_desc", new BigDecimal("59.99")), 4);
		StockItem stockItem2 = new StockItemImpl("si2", new Item("Item2", "Item2_desc", new BigDecimal("59.99")), 2);
		
		CartItem cartItem1 = new CartItem(stockItem1, 3, System.currentTimeMillis());
		CartItem cartItem2 = new CartItem(stockItem2, 2, System.currentTimeMillis());
		
		carts.add("user1", cartItem1);
		carts.add("user1", cartItem2);
		
		//decrease
		carts.setQuantity("user1", "si2", 1);
		
		assertEquals(1, cartItem2.getCartQuantity());
		
		//increase to max available
		carts.setQuantity("user1", "si2", 2);
		
		assertEquals(2, cartItem2.getCartQuantity());
		
		//Set quantity should not exceed available quantity 
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			carts.setQuantity("user1", "si2", 6);
		 });
	}
	

}
