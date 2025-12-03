package es.upm.grise.profundizacion.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTest {

	private Order order;
	private Item item;
	private Product product;

	@BeforeEach
	public void setUp() {
		order = new Order();
		item = mock(Item.class);
		product = mock(Product.class);
	}

	// Tests del constructor

	@Test
	public void testConstructorInitializesEmptyItemsList() {
		Order newOrder = new Order();
		assertNotNull(newOrder.getItems());
		assertEquals(0, newOrder.getItems().size());
	}

	// Tests de validación de precio

	@Test
	public void testAddItemThrowsExceptionWhenPriceIsNegative() {
		when(item.getPrice()).thenReturn(-1.0);
		when(item.getQuantity()).thenReturn(1);
		when(item.getProduct()).thenReturn(product);

		IncorrectItemException exception = assertThrows(IncorrectItemException.class, () -> {
			order.addItem(item);
		});

		assertTrue(exception.getMessage().contains("precio"));
	}

	@Test
	public void testAddItemAcceptsZeroPrice() throws IncorrectItemException {
		when(item.getPrice()).thenReturn(0.0);
		when(item.getQuantity()).thenReturn(1);
		when(item.getProduct()).thenReturn(product);

		order.addItem(item);

		assertEquals(1, order.getItems().size());
	}

	@Test
	public void testAddItemAcceptsPositivePrice() throws IncorrectItemException {
		when(item.getPrice()).thenReturn(10.0);
		when(item.getQuantity()).thenReturn(1);
		when(item.getProduct()).thenReturn(product);

		order.addItem(item);

		assertEquals(1, order.getItems().size());
	}

	// Tests de validación de cantidad

	@Test
	public void testAddItemThrowsExceptionWhenQuantityIsZero() {
		when(item.getPrice()).thenReturn(10.0);
		when(item.getQuantity()).thenReturn(0);
		when(item.getProduct()).thenReturn(product);

		IncorrectItemException exception = assertThrows(IncorrectItemException.class, () -> {
			order.addItem(item);
		});

		assertTrue(exception.getMessage().contains("cantidad"));
	}

	@Test
	public void testAddItemThrowsExceptionWhenQuantityIsNegative() {
		when(item.getPrice()).thenReturn(10.0);
		when(item.getQuantity()).thenReturn(-1);
		when(item.getProduct()).thenReturn(product);

		IncorrectItemException exception = assertThrows(IncorrectItemException.class, () -> {
			order.addItem(item);
		});

		assertTrue(exception.getMessage().contains("cantidad"));
	}

	@Test
	public void testAddItemAcceptsPositiveQuantity() throws IncorrectItemException {
		when(item.getPrice()).thenReturn(10.0);
		when(item.getQuantity()).thenReturn(5);
		when(item.getProduct()).thenReturn(product);

		order.addItem(item);

		assertEquals(1, order.getItems().size());
	}

	// Tests de añadir items nuevos

	@Test
	public void testAddItemAddsNewItemToEmptyList() throws IncorrectItemException {
		when(item.getPrice()).thenReturn(10.0);
		when(item.getQuantity()).thenReturn(1);
		when(item.getProduct()).thenReturn(product);
		when(product.getId()).thenReturn(1L);

		order.addItem(item);

		assertEquals(1, order.getItems().size());
		assertTrue(order.getItems().contains(item));
	}

	@Test
	public void testAddItemAddsMultipleDifferentProducts() throws IncorrectItemException {
		Item item1 = mock(Item.class);
		Product product1 = mock(Product.class);
		when(item1.getPrice()).thenReturn(10.0);
		when(item1.getQuantity()).thenReturn(1);
		when(item1.getProduct()).thenReturn(product1);
		when(product1.getId()).thenReturn(1L);

		Item item2 = mock(Item.class);
		Product product2 = mock(Product.class);
		when(item2.getPrice()).thenReturn(20.0);
		when(item2.getQuantity()).thenReturn(2);
		when(item2.getProduct()).thenReturn(product2);
		when(product2.getId()).thenReturn(2L);

		order.addItem(item1);
		order.addItem(item2);

		assertEquals(2, order.getItems().size());
	}

	// Tests de productos existentes con mismo precio

	@Test
	public void testAddItemIncrementsQuantityWhenProductAndPriceMatch() throws IncorrectItemException {
		Item item1 = mock(Item.class);
		Product product1 = mock(Product.class);
		when(item1.getPrice()).thenReturn(10.0);
		when(item1.getQuantity()).thenReturn(3);
		when(item1.getProduct()).thenReturn(product1);
		when(product1.getId()).thenReturn(1L);

		Item item2 = mock(Item.class);
		when(item2.getPrice()).thenReturn(10.0);
		when(item2.getQuantity()).thenReturn(2);
		when(item2.getProduct()).thenReturn(product1);

		order.addItem(item1);
		order.addItem(item2);

		assertEquals(1, order.getItems().size());
		verify(item1).setQuantity(5);
	}

	@Test
	public void testAddItemIncrementsQuantityMultipleTimes() throws IncorrectItemException {
		Item item1 = mock(Item.class);
		Product product1 = mock(Product.class);
		when(item1.getPrice()).thenReturn(15.0);
		when(item1.getQuantity()).thenReturn(6).thenReturn(8);
		when(item1.getProduct()).thenReturn(product1);
		when(product1.getId()).thenReturn(1L);

		Item item2 = mock(Item.class);
		when(item2.getPrice()).thenReturn(15.0);
		when(item2.getQuantity()).thenReturn(2);
		when(item2.getProduct()).thenReturn(product1);

		Item item3 = mock(Item.class);
		when(item3.getPrice()).thenReturn(15.0);
		when(item3.getQuantity()).thenReturn(2);
		when(item3.getProduct()).thenReturn(product1);

		order.addItem(item1);
		order.addItem(item2);
		order.addItem(item3);

		assertEquals(1, order.getItems().size());
		verify(item1, times(2)).setQuantity(anyInt());
	}

	// Tests de productos existentes con precio diferente

	@Test
	public void testAddItemCreatesNewEntryWhenProductExistsButPriceDiffers() throws IncorrectItemException {
		Item item1 = mock(Item.class);
		Product product1 = mock(Product.class);
		when(item1.getPrice()).thenReturn(10.0);
		when(item1.getQuantity()).thenReturn(1);
		when(item1.getProduct()).thenReturn(product1);
		when(product1.getId()).thenReturn(1L);

		Item item2 = mock(Item.class);
		when(item2.getPrice()).thenReturn(15.0);
		when(item2.getQuantity()).thenReturn(2);
		when(item2.getProduct()).thenReturn(product1);

		order.addItem(item1);
		order.addItem(item2);

		assertEquals(2, order.getItems().size());
		assertTrue(order.getItems().contains(item1));
		assertTrue(order.getItems().contains(item2));
	}

	@Test
	public void testAddItemHandlesMultipleSuppliersForSameProduct() throws IncorrectItemException {
		Product product1 = mock(Product.class);
		when(product1.getId()).thenReturn(1L);

		Item itemSupplier1 = mock(Item.class);
		when(itemSupplier1.getPrice()).thenReturn(10.0);
		when(itemSupplier1.getQuantity()).thenReturn(5);
		when(itemSupplier1.getProduct()).thenReturn(product1);

		Item itemSupplier2 = mock(Item.class);
		when(itemSupplier2.getPrice()).thenReturn(12.0);
		when(itemSupplier2.getQuantity()).thenReturn(3);
		when(itemSupplier2.getProduct()).thenReturn(product1);

		Item itemSupplier3 = mock(Item.class);
		when(itemSupplier3.getPrice()).thenReturn(9.5);
		when(itemSupplier3.getQuantity()).thenReturn(10);
		when(itemSupplier3.getProduct()).thenReturn(product1);

		order.addItem(itemSupplier1);
		order.addItem(itemSupplier2);
		order.addItem(itemSupplier3);

		assertEquals(3, order.getItems().size());
	}

	// Tests de casos complejos

	@Test
	public void testAddItemComplexScenarioWithMultipleProductsAndPrices() throws IncorrectItemException {
		Product product1 = mock(Product.class);
		Product product2 = mock(Product.class);
		when(product1.getId()).thenReturn(1L);
		when(product2.getId()).thenReturn(2L);

		Item item1 = mock(Item.class);
		when(item1.getPrice()).thenReturn(10.0);
		when(item1.getQuantity()).thenReturn(4);
		when(item1.getProduct()).thenReturn(product1);

		Item item2 = mock(Item.class);
		when(item2.getPrice()).thenReturn(10.0);
		when(item2.getQuantity()).thenReturn(2);
		when(item2.getProduct()).thenReturn(product1);

		Item item3 = mock(Item.class);
		when(item3.getPrice()).thenReturn(15.0);
		when(item3.getQuantity()).thenReturn(1);
		when(item3.getProduct()).thenReturn(product1);

		Item item4 = mock(Item.class);
		when(item4.getPrice()).thenReturn(20.0);
		when(item4.getQuantity()).thenReturn(3);
		when(item4.getProduct()).thenReturn(product2);

		order.addItem(item1);
		order.addItem(item2);
		order.addItem(item3);
		order.addItem(item4);

		assertEquals(3, order.getItems().size());
		verify(item1).setQuantity(6);
	}

}
