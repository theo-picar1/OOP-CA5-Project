package com.example.oopca5project;

import com.example.oopca5project.DAOs.*;
import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.Exceptions.DaoException;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class MainAppTest {

    static ProductDaoInterface IProductDao = new MySqlProductDao();
    static SupplierDaoInterface ISupplierDao = new MySqlSupplierDao();
    static CustomerDaoInterface ICustomerDao = new MySqlCustomerDao();
    static CustomersProductsDaoInterface ICustomersProductsDao = new MySqlCustomersProductsDao();

    @Test
// Tests if getAllProducts() returns an empty list (null check)
    void getAllProductsReturnsNull() {
        List<Product> products = null;
        try {
            products = IProductDao.getAllProducts();
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNotNull(products);
        assertFalse(products.isEmpty(), "Expected empty list but got: " + products);
    }

    // Tests if getAllProducts() returns the expected products
    @Test
    void getAllProductsReturnsProducts() {
        List<Product> products = null;
        try {
            products = IProductDao.getAllProducts();
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
        String validProductId = "product1"; // Use a valid product ID from the database

        Product product = null;
        try {
            product = IProductDao.getProductById(validProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals("product1", validProductId, product.getId());
    }

    @Test
    void getProductByIdReturnsNullForInvalidId() {
        String invalidProductId = "product0";

        Product product = null;
        try {
            product = IProductDao.getProductById(invalidProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNull(product, "Expected null when product is not found");
    }

    // ***************************
    // ***** FEATURE 3 TESTS *****

    @Test
    void deleteProductByIdDeletesProduct() {
        String validProductId = "product2";

        int rowsAffected = 0;
        try {
            rowsAffected = IProductDao.deleteProductById(validProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(1, 1, rowsAffected);
    }

    @Test
    void deleteProductByIdReturnsZeroForInvalidId() {
        String invalidProductId = "product0";

        int rowsAffected = 0;
        try {
            rowsAffected = IProductDao.deleteProductById(invalidProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(0, 0, rowsAffected);
    }

    // ***************************
    // ***** FEATURE 4 TESTS *****

    @Test
    void addProductWasntAdded() throws DaoException {
        Product product = new Product("test1", "", "", 1, "");

        int actual = 0;
        try {
            IProductDao.addProduct(product);
            actual = IProductDao.addProduct(product);
        } catch (DaoException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IProductDao.deleteProductById("test1");
        }
        assertEquals(0, actual);
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
        Product product = new Product("test3", "", "", 1, "supplier1");

        int actual = 0;
        try {
            actual = IProductDao.addProduct(product);
            IProductDao.deleteProductById("test3");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, actual);
    }

    @Test
    void addProductWasntAddedForeginKey() {
        Product product = new Product("test3", "j", "k", 1, "supp");

        int actual = 0;
        try {
            actual = IProductDao.addProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, actual);
    }

    // ***************************
    // ***** FEATURE 5 TESTS *****

    @Test
    void updateProductWasntUpdatedAndIdNull() {
        try {
            Product p = IProductDao.getProductById(null);
            IProductDao.updateProduct(null, p);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void updateProductWasntUpdatedAndIdWrong() {
        try {
            Product p = IProductDao.getProductById("1");
            IProductDao.updateProduct("1", p);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void updateProductWasUpdated() {
        int actual = 0;
        Product p2 = new Product("test1", "", "", 1, "supplier1");
        try {
            Product p1 = IProductDao.getProductById("product3");
            actual = IProductDao.updateProduct("product1", p2);
            IProductDao.updateProduct("product1", p1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, actual);
    }

    // *************** FILTER PRODUCTS LESS THAN PRICE TEST ***************
    @Test
    // Tests to see if filteredProducts will return nothing if provided with no products to filter by (no match test)
    void filterProductsTest1() {
        double price = 10.00;
        List<Product> products = new ArrayList<>();
        List<Product> filteredProducts = Product.filterProductsByPrice(price, products);

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

        List<Product> filteredProducts = Product.filterProductsByPrice(price, products);

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

        List<Product> filteredProducts = Product.filterProductsByPrice(price, products);

        int expected = 2;
        int actual = filteredProducts.size();

        assertEquals(expected, actual);
    }
    // ***************************************************************

    // *************** PRODUCT LISTS TO JSON STRING TESTS ***************
    @Test
    void productsListToJsonStringNullList() {
        List<Product> list = null;

        assertNull(Product.productsListToJsonString(list));
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

        assertEquals(jsonArray.toString(), Product.productsListToJsonString(list).toString());
    }
    // ************************************************************

    // **************** PRODUCT TO JSON OBJECT TESTS ****************
    @Test
    // Tests to see if null is returned when null is sent in as a product
    void oneProductToJsonTest1() {
        Product product = null;

        assertNull(Product.turnProductIntoJson(product));
    }

    @Test
        // Tests to see if null is NOT returned (which will be assumed that the product was successfully turned into a JSON object)
    void oneProductToJsonTest2() {
        Product product = new Product("testProduct", "testDescription", "testSize", 20.25, "testSupplier");

        assertNotNull(Product.turnProductIntoJson(product));
    }
    // *******************************************************************

    // *************** ADD SUPPLIER TESTS ********************
    @Test
    void addSupplierTest1() { // Tests to see if a new supplier is successfully added
        try {
            Supplier supplier = new Supplier("JunitSupplier", "JunitSupplier", "JunitSupplier", "JunitSupplier");
            int expected = 1;
            int actual = ISupplierDao.addSupplier(supplier);

            assertEquals(expected, actual);
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
        // Delete the newly created supplier made by the test
        finally {
            try {
                ISupplierDao.deleteSupplierById("JunitSupplier");
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void addSupplierTest2() { // Tests to see if a product will not be added if there is a duplicate supplier
        try {
            Supplier supplier = new Supplier("JunitSupplier", "JunitSupplier", "JunitSupplier", "JunitSupplier");
            ISupplierDao.addSupplier(supplier);
            int expected = 0;
            int actual = ISupplierDao.addSupplier(supplier); // Try adding the supplier again. Shpuld not be added to the table
            assertEquals(expected, actual);
        }
        catch (DaoException e) {
            System.out.println("addSupplierTest2() successful!");
        }
        finally {
            try {
                ISupplierDao.deleteSupplierById("JunitTest");
            }
            catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }
    // ****************************************************

    // *************** ADD CUSTOMER TESTS ********************
    @Test
    void addCustomerTest1() { // Tests to see if a new customer is successfully added
        try {
            Customer customer = new Customer(100000, "JunitCustomer", "JunitCustomer", "JunitCustomer");
            int expected = 1;
            int actual = ICustomerDao.addCustomer(customer);

            assertEquals(expected, actual);
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
        finally {
            try {
                ICustomerDao.deleteCustomerById(100000);
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void addCustomerTest2() { // Tests to see if a customer will not be added if there is a duplicate supplier
        try {
            Customer customer = new Customer(100000, "JunitCustomer", "JunitCustomer", "JunitCustomer");
            ICustomerDao.addCustomer(customer);
            int expected = 0;
            int actual = ICustomerDao.addCustomer(customer); // Try adding the customer again. Should not be added to the table
            assertEquals(expected, actual);
        }
        catch (DaoException e) {
            System.out.println("addCustomerTest2() successful!");
        }
        finally {
            try {
                ICustomerDao.deleteCustomerById(100000);
            }
            catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }
    // ****************************************************
}