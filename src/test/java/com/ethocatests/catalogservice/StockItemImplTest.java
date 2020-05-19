package com.ethocatests.catalogservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ethocatests.catalogservice.store.StockItem;
import com.ethocatests.catalogservice.store.data.Item;
import com.ethocatests.catalogservice.store.impl.StockItemImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockItemImplTest {
	
	@Test
	void testSubtructQuantity() {
		StockItem stockItem = new StockItemImpl("good1", 
										   new Item("American Girl Wellywisher Doll", 
				                      				"American Girl Wellywisher Doll, an iconic toy for many generations, with a new fresh look.",
				                      				new BigDecimal("59.99")),
										   3);
		//allows subtraction when available qty => subtraction qty
		assertTrue(stockItem.subtractQuantity(1));
		
		assertEquals(2, stockItem.availableQuantity());
		
		//doesn't allow to subtract when available qty < subtraction qty
		assertFalse(stockItem.subtractQuantity(3));
		
		assertEquals(2, stockItem.availableQuantity());
	}

}
