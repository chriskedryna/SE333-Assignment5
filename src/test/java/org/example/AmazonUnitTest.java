package org.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.List;

import org.example.Amazon.*;
import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmazonUnitTest {

    // AMAZON UNIT TESTS

    @Test
    @DisplayName("structural-based")
    void calculateTest() {
        PriceRule mockPriceRule = mock(PriceRule.class);
        when(mockPriceRule.priceToAggregate(anyList())).thenReturn(100.0);
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        when(shoppingCart.getItems()).thenReturn(List.of(new Item(ItemType.OTHER, "Test Item", 10, 1.0)));
        Amazon amazon = new Amazon(shoppingCart, List.of(mockPriceRule));
        double finalPrice = amazon.calculate();
        assertEquals(100.0, finalPrice);
    }

    @Test
    @DisplayName("structural-based")
    void addToCartTest() {
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        Amazon amazon = new Amazon(shoppingCart, List.of());
        Item item = new Item(ItemType.OTHER, "Test Item", 10, 1.0);
        amazon.addToCart(item);
        verify(shoppingCart, times(1)).add(item);
    }

    // DATABASE UNIT TESTS

    @Test
    @DisplayName("structural-based")
    void getConnectionTest() {
        Database database = new Database();
        assertNotNull(database.getConnection());
    }

    @Test
    @DisplayName("structural-based")
    void resetDatabaseTest() {
        Database database = new Database();
        database.resetDatabase();
        // We can verify that the connection is still valid after resetting the database
        assertNotNull(database.getConnection());
    }

    @Test
    @DisplayName("structural-based")
    void closeTest() {
        Database database = new Database();
        database.close();
        // We can verify that the connection is closed by checking if it throws an
        // exception when we try to use it
        assertNull(database.getConnection());
    }

    // ITEM UNIT TESTS

    @Test
    @DisplayName("specification-based")
    void getTypeTest() {
        Item item = new Item(ItemType.ELECTRONIC, "Test Item", 10, 1.0);
        assertEquals(ItemType.ELECTRONIC, item.getType());
    }

    @Test
    @DisplayName("specification-based")
    void getNameTest() {
        Item item = new Item(ItemType.ELECTRONIC, "Test Item", 10, 1.0);
        assertEquals("Test Item", item.getName());
    }

    @Test
    @DisplayName("specification-based")
    void getQuantityTest() {
        Item item = new Item(ItemType.ELECTRONIC, "Test Item", 10, 1.0);
        assertEquals(10, item.getQuantity());
    }

    @Test
    @DisplayName("specification-based")
    void getPricePerUnitTest() {
        Item item = new Item(ItemType.ELECTRONIC, "Test Item", 10, 1.0);
        assertEquals(1.0, item.getPricePerUnit());
    }

    // SHOPPING CART UNIT TESTS

    @Test
    @DisplayName("specification-based")
    void addTest() {
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        Item item = new Item(ItemType.OTHER, "Test Item", 10, 1.0);
        shoppingCart.add(item);
        verify(shoppingCart, times(1)).add(item);
    }

    @Test
    @DisplayName("specification-based")
    void getItemsTest() {
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        Item item = new Item(ItemType.OTHER, "Test Item", 10, 1.0);
        shoppingCart.add(item);
        when(shoppingCart.getItems()).thenReturn(List.of(item));
        List<Item> items = shoppingCart.getItems();
        assertEquals(List.of(item), items);
    }

    @Test
    @DisplayName("specification-based")
    void numberOfItemsTest() {
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        Item item = new Item(ItemType.OTHER, "Test Item", 10, 1.0);
        shoppingCart.add(item);
        when(shoppingCart.numberOfItems()).thenReturn(1);
        assertEquals(1, shoppingCart.numberOfItems());
    }

    // SHOPPING CART ADAPTOR UNIT TESTS

    @Test
    @DisplayName("specification-based")
    void ShoppingCartAdaptorTest() {
        Database database = new Database();
        ShoppingCartAdaptor shoppingCartAdaptor = new ShoppingCartAdaptor(database);
        assertNotNull(shoppingCartAdaptor);
    }

    // DELIVERY PRICE UNIT TESTS

    @Test
    @DisplayName("specification-based")
    void deliveryPriceTest1() {
        List<Item> cart = List.of(new Item(ItemType.OTHER, "Test Item", 1, 1.0),
                new Item(ItemType.OTHER, "Test Item 2", 1, 1.0));
        DeliveryPrice deliveryPrice = new DeliveryPrice();
        assertEquals(5, deliveryPrice.priceToAggregate(cart));
    }

    @Test
    @DisplayName("specification-based")
    void deliveryPriceTest2() {
        List<Item> cart = List.of();
        DeliveryPrice deliveryPrice = new DeliveryPrice();
        assertEquals(0, deliveryPrice.priceToAggregate(cart));
    }

    @Test
    @DisplayName("specification-based")
    void deliveryPriceTest3() {
        List<Item> cart = List.of(new Item(ItemType.OTHER, "Test Item", 1, 1.0),
                new Item(ItemType.OTHER, "Test Item 2", 1, 1.0), new Item(ItemType.OTHER, "Test Item 3", 1, 1.0),
                new Item(ItemType.OTHER, "Test Item 4", 1, 1.0));
        DeliveryPrice deliveryPrice = new DeliveryPrice();
        assertEquals(12.5, deliveryPrice.priceToAggregate(cart));
    }

    @Test
    @DisplayName("specification-based")
    void deliveryPriceTest4() {
        List<Item> cart = List.of(new Item(ItemType.OTHER, "Test Item", 1, 1.0),
                new Item(ItemType.OTHER, "Test Item 2", 1, 1.0), new Item(ItemType.OTHER, "Test Item 3", 1, 1.0),
                new Item(ItemType.OTHER, "Test Item 4", 1, 1.0), new Item(ItemType.OTHER, "Test Item", 1, 1.0),
                new Item(ItemType.OTHER, "Test Item 2", 1, 1.0), new Item(ItemType.OTHER, "Test Item 3", 1, 1.0),
                new Item(ItemType.OTHER, "Test Item 4", 1, 1.0), new Item(ItemType.OTHER, "Test Item", 1, 1.0),
                new Item(ItemType.OTHER, "Test Item 2", 1, 1.0), new Item(ItemType.OTHER, "Test Item 3", 1, 1.0),
                new Item(ItemType.OTHER, "Test Item 4", 1, 1.0));
        DeliveryPrice deliveryPrice = new DeliveryPrice();
        assertEquals(20.0, deliveryPrice.priceToAggregate(cart));
    }

    // EXTRA COST FOR ELECTRONICS UNIT TESTS

    @Test
    @DisplayName("specification-based")
    void extraCostForElectronicsTest1() {
        List<Item> cart = List.of(new Item(ItemType.ELECTRONIC, "Test Item", 1, 1.0));
        ExtraCostForElectronics extraCostForElectronics = new ExtraCostForElectronics();
        assertEquals(7.50, extraCostForElectronics.priceToAggregate(cart));
    }

    @Test
    @DisplayName("specification-based")
    void extraCostForElectronicsTest2() {
        List<Item> cart = List.of(new Item(ItemType.OTHER, "Test Item", 1, 1.0));
        ExtraCostForElectronics extraCostForElectronics = new ExtraCostForElectronics();
        assertEquals(0.0, extraCostForElectronics.priceToAggregate(cart));
    }

    // REGULAR COST UNIT TESTS

    @Test
    @DisplayName("specification-based")
    void regularCostTest() {
        List<Item> cart = List.of(new Item(ItemType.OTHER, "Test Item", 2, 1.0),
                new Item(ItemType.OTHER, "Test Item 2", 3, 2.0));
        RegularCost regularCost = new RegularCost();
        assertEquals(8.0, regularCost.priceToAggregate(cart));
    }

}
