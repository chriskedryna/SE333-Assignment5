package org.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.List;

import org.example.Amazon.*;
import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmazonIntegrationTest {

    private static Database database;

    @BeforeEach
    void setUp() {
        database = new Database();
        database.resetDatabase();
    }

    @Test
    @DisplayName("structural-based")
    void calculateIntegrationTest() {
        Database database = new Database();
        ShoppingCartAdaptor shoppingCart = new ShoppingCartAdaptor(database);
        Item item1 = new Item(ItemType.OTHER, "Test Item 1", 2, 5.0);
        Item item2 = new Item(ItemType.ELECTRONIC, "Test Item 2", 1, 10.0);
        shoppingCart.add(item1);
        shoppingCart.add(item2);
        Amazon amazon = new Amazon(shoppingCart,
                List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics()));
        double finalPrice = amazon.calculate();
        assertEquals(32.50, finalPrice);
    }

    @Test
    @DisplayName("structural-based")
    void calculateIntegrationTestEmptyCart() {
        Database database = new Database();
        ShoppingCartAdaptor shoppingCart = new ShoppingCartAdaptor(database);
        Amazon amazon = new Amazon(shoppingCart,
                List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics()));
        double finalPrice = amazon.calculate();
        assertEquals(0.0, finalPrice);
    }

    @Test
    @DisplayName("structural-based")
    void calculateIntegrationTestLargeCart() {
        Database database = new Database();
        ShoppingCartAdaptor shoppingCart = new ShoppingCartAdaptor(database);
        for (int i = 0; i < 15; i++) {
            shoppingCart.add(new Item(ItemType.OTHER, "Test Item " + i, 1, 1.0));
        }
        Amazon amazon = new Amazon(shoppingCart,
                List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics()));
        double finalPrice = amazon.calculate();
        assertEquals(35.0, finalPrice);
    }

    @Test
    @DisplayName("structural-based")
    void numberOfItemsIntegrationTest() {
        Database database = new Database();
        ShoppingCartAdaptor shoppingCart = new ShoppingCartAdaptor(database);
        shoppingCart.add(new Item(ItemType.OTHER, "Test Item 1", 1, 5.0));
        shoppingCart.add(new Item(ItemType.ELECTRONIC, "Test Item 2", 1, 10.0));
        shoppingCart.add(new Item(ItemType.ELECTRONIC, "Test Item 3", 1, 15.0));
        assertEquals(3, shoppingCart.getItems().size());
    }

    @Test
    @DisplayName("structural-based")
    void getItemsIntegrationTest() {
        Database database = new Database();
        ShoppingCartAdaptor shoppingCart = new ShoppingCartAdaptor(database);
        Item item1 = new Item(ItemType.OTHER, "Test Item 1", 1, 5.0);
        Item item2 = new Item(ItemType.ELECTRONIC, "Test Item 2", 1, 10.0);
        shoppingCart.add(item1);
        shoppingCart.add(item2);
        List<Item> items = shoppingCart.getItems();
        assertEquals(item1.getName(), items.get(0).getName());
        assertEquals(item2.getName(), items.get(1).getName());
    }
}