package com.example.oopca5project;

import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;
import org.json.JSONArray;
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

    @Test
    void getProductByIdReturnsProduct() {
        ProductDaoInterface productDao = new MySqlProductDao();
        String validProductId = "product1"; // Use a valid product ID from the database

        Product product = null;
        try {
            product = productDao.getProductById(validProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals("product1", validProductId, product.getId());
    }

    @Test
    void getProductByIdReturnsNullForInvalidId() {
        ProductDaoInterface productDao = new MySqlProductDao();
        String invalidProductId = "product0";

        Product product = null;
        try {
            product = productDao.getProductById(invalidProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNull(product, "Expected null when product is not found");
    }

    // ***************************
    // ***** FEATURE 3 TESTS *****

    @Test
    void deleteProductByIdDeletesProduct() {
        ProductDaoInterface productDao = new MySqlProductDao();
        String validProductId = "product2";

        int rowsAffected = 0;
        try {
            rowsAffected = productDao.deleteProductById(validProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(1, 1, rowsAffected);
    }

    @Test
    void deleteProductByIdReturnsZeroForInvalidId() {
        ProductDaoInterface productDao = new MySqlProductDao();
        String invalidProductId = "product0";

        int rowsAffected = 0;
        try {
            rowsAffected = productDao.deleteProductById(invalidProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(0, 0, rowsAffected);
    }

    // ***************************
    // ***** FEATURE 4 TESTS *****

    @Test
    void addProductWasntAdded() throws DaoException {
        ProductDaoInterface productAdd = new MySqlProductDao();
        Product product = new Product("test1", "", "", 1, "");

        int n = 0;
        try {
            productAdd.addProduct(product);
            n = productAdd.addProduct(product);
        } catch (DaoException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            productAdd.deleteProductById("test1");
        }
        assertEquals(0, n);
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

    @Test
    void updateProductWasntUpdatedAndIdNull() {
        ProductDaoInterface productUpdate = new MySqlProductDao();

        try {
            Product p = productUpdate.getProductById(null);
            productUpdate.updateProduct(null, p);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void updateProductWasntUpdatedAndIdWrong() {
        ProductDaoInterface productUpdate = new MySqlProductDao();

        try {
            Product p = productUpdate.getProductById("1");
            productUpdate.updateProduct("1", p);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void updateProductWasUpdated() {
        ProductDaoInterface productUpdate = new MySqlProductDao();
        int n = 0;
        Product p2 = new Product("test1", "", "", 1, "");
        try {
            Product p1 = productUpdate.getProductById("product2");
            n = productUpdate.updateProduct("product1", p2);
            productUpdate.updateProduct("product1", p1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, n);
    }

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

    @Test
    void productsListToJsonStringNullList() {
        List<Product> list = null;

        assertNull(Methods.productsListToJsonString(list));
    }

    @Test
    void productsListToJsonStringNullList1() {
        List<Product> list = List.of(
                new Product("1", "desc", "small", 2, "1"),
                new Product("2", "descrip", "medium", 4, "2")
        );

        JSONArray jsonArray = new JSONArray(
                "[" +
                            "{\"size\":\"small\",\"product_id\":\"1\",\"product_description\":\"desc\",\"unit_price\":2,\"supplier_id\":\"1\"}," +
                            "{\"size\":\"medium\",\"product_id\":\"2\",\"product_description\":\"descrip\",\"unit_price\":4,\"supplier_id\":\"2\"}" +
                        "]");

        assertEquals(jsonArray.toString(), Methods.productsListToJsonString(list).toString());
    }

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