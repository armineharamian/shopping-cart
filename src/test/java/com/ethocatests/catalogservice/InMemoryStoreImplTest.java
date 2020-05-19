package com.ethocatests.catalogservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ethocatests.catalogservice.store.StockItem;
import com.ethocatests.catalogservice.store.impl.InMemoryStoreImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
class InMemoryStoreImplTest {
	
	@Autowired
	InMemoryStoreImpl store;

	@Test
	void testCatalog() {
		List<? extends StockItem> stock = store.catalog();
		assertEquals(2, stock.size());
	}
	
	@Test
	void testAddToCart() {
		//add 1 item
		assertTrue(store.addToCart("user1", "good1", 1));
		
		//1 item added
		assertEquals(1, store.cart("user1").items().size());
		
		//add to cart quantity is more than available
		assertFalse(store.addToCart("user1", "good2", 10));
		
		//new item is not added
		assertEquals(1, store.cart("user1").items().size());
		
		//stock item doesn't exist
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			 store.addToCart("user1", "non_existing_stock_item", 1);
		 });	
	}
	
	@Test
	void testCart() {
		//cart doesn't exist for the user
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			 store.cart("non_existing_user");
		 });
		
		store.addToCart("new_user", "good1", 1);
		
		Assertions.assertDoesNotThrow(() -> store.cart("new_user"));
		
	}
	
	@Test
	void testCheckout() {
		//create a cart for userA
		store.addToCart("userA", "good1", 1);
		store.addToCart("userA", "good2", 2);
		
		//create a cart for userB
		store.addToCart("userB", "good1", 1);
		
		//create a cart for userC
		store.addToCart("userC", "good2", 1);
		
		//Checkout userA
		store.checkout("userA");
		
		//no more cart for 'userA'
		//sets good1 availability to 2 , e g 3-1
		// good2 availability to 0, e g 2-2
		Assertions.assertThrows(IllegalArgumentException.class, () -> store.cart("userA"));
	
		//Still 2 of 'good1's are available
		Assertions.assertDoesNotThrow(() -> store.checkout("userB"));
		
		//available quantity is less than added to the UserC's cart
		Assertions.assertThrows(IllegalStateException.class, () -> store.checkout("userC"));
		
	}
	
}
