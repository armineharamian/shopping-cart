package com.ethocatests.catalogservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ethocatests.catalogservice.store.StockItem;
import com.ethocatests.catalogservice.store.data.CartItem;
import com.ethocatests.catalogservice.store.data.Item;
import com.ethocatests.catalogservice.store.impl.InMemoryCart;
import com.ethocatests.catalogservice.store.impl.StockItemImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InMemoryCartImplTest {

	
	
	@Test
	void testAddItemToCart() {
		StockItem stockItem1 = new StockItemImpl("si1", new Item("Item1", "Item1_desc", new BigDecimal("59.99")), 3);
		StockItem stockItem2 = new StockItemImpl("si2", new Item("Item2", "Item2_desc", new BigDecimal("9.99")), 20);
		
		InMemoryCart cart = new InMemoryCart();
		
		cart.add(new CartItem(stockItem1, 2, System.currentTimeMillis()));
		cart.add(new CartItem(stockItem2, 1, System.currentTimeMillis()));
		
		assertEquals(2, cart.items().size());
	}
	
	@Test 
	void testNewCartIsEmpty() {
		InMemoryCart cart = new InMemoryCart();
		
		//empty cart 
		assertTrue(cart.items().isEmpty());
	}
	
	@Test
	void testGetCartItem() {
		StockItem stockItem1 = new StockItemImpl("si1", new Item("Item1", "Item1_desc", new BigDecimal("59.99")), 3);
		StockItem stockItem2 = new StockItemImpl("si2", new Item("Item2", "Item2_desc", new BigDecimal("9.99")), 20);
		
		InMemoryCart cart = new InMemoryCart();
		
		cart.add(new CartItem(stockItem1, 2, System.currentTimeMillis()));
		cart.add(new CartItem(stockItem2, 1, System.currentTimeMillis()));
		
		assertEquals("Item1", cart.get("si1").getTitle());
	}
	
	@Test
	void testRemoveItemFromCart() {
		StockItem stockItem1 = new StockItemImpl("si1", new Item("Item1", "Item1_desc", new BigDecimal("59.99")), 3);
		StockItem stockItem2 = new StockItemImpl("si2", new Item("Item2", "Item2_desc", new BigDecimal("9.99")), 20);
		
		InMemoryCart cart = new InMemoryCart();
		
		cart.add(new CartItem(stockItem1, 2, System.currentTimeMillis()));
		cart.add(new CartItem(stockItem2, 1, System.currentTimeMillis()));
		
		cart.remove("si2");
		
		//Only one item left and the item is Item1
		assertEquals(1, cart.items().size());
		assertEquals("Item1", cart.items().get(0).getTitle());
	}
}
