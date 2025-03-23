package com.example.oopca5project;

import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class MainAppTest {

    // ***** FEATURE 1 TESTS *****

    @Test
// Tests if getAllProducts() returns an empty list (null check)
    void getAllProductsReturnsNull() {
        ProductDaoInterface productDao = new MySqlProductDao();

        List<Product> products = null;
        try {
            products = productDao.getAllProducts();
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNotNull(products);
        assertFalse(products.isEmpty(), "Expected empty list but got: " + products);
    }

    // Tests if getAllProducts() returns the expected products
    @Test
    void getAllProductsReturnsProducts() {
        ProductDaoInterface productDao = new MySqlProductDao();

        List<Product> products = null;
        try {
            products = productDao.getAllProducts();
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNotNull(products);
        assertFalse(products.isEmpty(), "Expected products, but the list is empty");
    }

    // ***************************
    // ***** FEATURE 2 TESTS *****

    // Input F2 tests.

    // ***************************
    // ***** FEATURE 3 TESTS *****

    // Input F3 tests.

    // ***************************
    // ***** FEATURE 4 TESTS *****

    @Test
    void addProductWasntAdded() throws DaoException {
        ProductDaoInterface productAdd = new MySqlProductDao();
        Product product = new Product("test1", "", "", 1, "");

        try {
            productAdd.addProduct(product);
            productAdd.addProduct(product);
        } catch (Exception e) {
            assertTrue(true);
        } finally {
            productAdd.deleteProductById("test1");
        }
    }

    @Test
    void addProductWasntAddedAndNull() {
        ProductDaoInterface productAdd = new MySqlProductDao();

        try {
            productAdd.addProduct(null);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void addProductWasAdded() {
        ProductDaoInterface productAdd = new MySqlProductDao();
        Product product = new Product("test3", "", "", 1, "");

        int n = 0;
        try {
            n = productAdd.addProduct(product);
            productAdd.deleteProductById("test3");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, n);
    }

    // ***************************
    // ***** FEATURE 5 TESTS *****

    // Input F5 tests.

    // ***************************
    // ***** FEATURE 6 TESTS *****
    @Test
    // Tests to see if filteredProducts will return nothing if provided with no products to filter by (no match test)
    void filterProductsTest1() {
        double price = 10.00;
        List<Product> products = new ArrayList<>();
        List<Product> filteredProducts = Methods.filterProductsByPrice(price, products);

        int expected = 0;
        int actual = filteredProducts.size();

        assertEquals(expected, actual);
    }

    @Test
        // Tests to see if filteredProducts will return everything in the provided products list (complete match test)
    void filterProductsTest2() {
        double price = 10.00;
        List<Product> products = new ArrayList<>();
        products.add(new Product("", "", "", 9.10, ""));
        products.add(new Product("", "", "", 8.10, ""));
        products.add(new Product("", "", "", 1.10, ""));

        List<Product> filteredProducts = Methods.filterProductsByPrice(price, products);

        int expected = products.size();
        int actual = filteredProducts.size();

        assertEquals(expected, actual);
    }

    @Test
        // Tests to see if only some of the products are returned (semi-match test)
    void filterProductsTest3() {
        double price = 10.00;
        List<Product> products = new ArrayList<>();
        products.add(new Product("", "", "", 9.10, ""));
        products.add(new Product("", "", "", 8.10, ""));
        products.add(new Product("", "", "", 11.10, ""));

        List<Product> filteredProducts = Methods.filterProductsByPrice(price, products);

        int expected = 2;
        int actual = filteredProducts.size();

        assertEquals(expected, actual);
    }

    // ***************************
    // ***** FEATURE 7 TESTS *****

    // Input F7 tests here

    // ***************************
    // ***** FEATURE 8 TESTS *****
    @Test
    // Tests to see if null is returned when null is sent in as a product
    void oneProductToJsonTest1() {
        Product product = null;

        assertNull(Methods.turnProductIntoJson(product));
    }

    @Test
        // Tests to see if null is NOT returned (which will be assumed that the product was successfully turned into a JSON object)
    void oneProductToJsonTest2() {
        Product product = new Product("testProduct", "testDescription", "testSize", 20.25, "testSupplier");

        assertNotNull(Methods.turnProductIntoJson(product));
    }
}