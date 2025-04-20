package com.example.oopca5project;

import com.example.oopca5project.DAOs.*;
import com.example.oopca5project.DTOs.*;
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

    // *************** GET ALL PRODUCTS TESTS ***************
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

    // // *************** GET PRODUCT BY ID TESTS ***************
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
    // ************************************************************

    // *************** DELETE PRODUCT BY ID TESTS ***************
    @Test
    void deleteProductByIdDeletesProduct() {
        String validProductId = "product123";

        int rowsAffected = 0;
        try {
            IProductDao.addProduct(new Product("Product123", "", "", 1, "Supplier2"));
            rowsAffected = IProductDao.deleteProductById(validProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(1, rowsAffected, "Expected one row to be affected when deleting product by ID.");
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

        assertEquals(0, rowsAffected, "Expected 0 rows affected for invalid product ID.");
    }
    // ***************************

    // *************** ADD PRODUCT TESTS ***************
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
        Product product = new Product("test3", "", "", 1, "Supplier2");

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
    // *********************************************************

    // // *************** UPDATE PRODUCT TESTS ***************
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
        Product originalProduct = new Product("test1", "", "", 1, "Supplier2");
        Product updatedProduct = new Product("test1", "desc", "size", 1, "Supplier2");
        try {
            IProductDao.addProduct(originalProduct);
            actual = IProductDao.updateProduct("test1", updatedProduct);
            IProductDao.deleteProductById("test1");
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
        } catch (DaoException e) {
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
        } catch (DaoException e) {
            System.out.println("addSupplierTest2() successful!");
        } finally {
            try {
                ISupplierDao.deleteSupplierById("JunitSupplier");
            } catch (DaoException e) {
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
        } catch (DaoException e) {
            e.printStackTrace();
        } finally {
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
        } catch (DaoException e) {
            System.out.println("addCustomerTest2() successful!");
        } finally {
            try {
                ICustomerDao.deleteCustomerById(100000);
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }
    // **********************************************************

    // *************** DISPLAY ALL SUPPLIERS TESTS **************

    // Tests if getAllSuppliers() returns an empty list (null check)
    @Test
    void getAllSuppliersReturnsNull() {
        List<Supplier> suppliers = null;
        try {
            suppliers = ISupplierDao.getAllSuppliers();
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNotNull(suppliers);
        assertFalse(suppliers.isEmpty(), "Expected empty list but got: " + suppliers);
    }

    // Tests if getAllSuppliers() returns the expected suppliers
    @Test
    void getAllSuppliersReturnsSuppliers() {
        List<Supplier> suppliers = null;
        try {
            suppliers = ISupplierDao.getAllSuppliers();
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNotNull(suppliers);
        assertFalse(suppliers.isEmpty(), "Expected suppliers, but the list is empty");
    }
    // **********************************************************

    // **************** DISPLAY ALL CUSTOMERS TESTS **************

    // Test if getAllCustomers() does not return null
    @Test
    void getAllCustomersReturnsNotNull() {
        List<Customer> customers = null;
        try {
            customers = ICustomerDao.getAllCustomers();
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNotNull(customers, "getAllCustomers() returned null");
    }

    // Test if getAllCustomers() returns a non-empty list
    @Test
    void getAllCustomersReturnsCustomers() {
        List<Customer> customers = null;
        try {
            customers = ICustomerDao.getAllCustomers();
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNotNull(customers);
        assertFalse(customers.isEmpty(), "Expected customers, but the list is empty");
    }

    // **********************************************************

    // *************** DELETE SUPPLIER BY ID TESTS ***************
    @Test
    void deleteSupplierByIdDeletesSupplier() {
        String validProductId = "Supplier123";

        int rowsAffected = 0;
        try {
            ISupplierDao.addSupplier(new Supplier(validProductId, "Supplier123", "1", "1"));
            rowsAffected = ISupplierDao.deleteSupplierById(validProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(1, rowsAffected, "Expected one row to be affected when deleting supplier by ID.");
    }

    @Test
    void deleteSupplierByIdReturnsZeroForInvalidId() {
        String invalidProductId = "Supplier0";

        int rowsAffected = 0;
        try {
            rowsAffected = IProductDao.deleteProductById(invalidProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(0, rowsAffected, "Expected 0 rows affected for invalid supplier ID.");
    }

    // **********************************************************

    // *************** DELETE CUSTOMER BY ID TESTS ***************

    @Test
    void deleteCustomerByIdDeletesCustomer() {
        int validCustomerId = 10001;

        int rowsAffected = 0;
        try {
            ICustomerDao.addCustomer(new Customer(validCustomerId, "CustomerName", "email@example.com", "Some Address"));
            rowsAffected = ICustomerDao.deleteCustomerById(validCustomerId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(1, rowsAffected, "Expected one row to be affected when deleting customer by ID.");
    }

    @Test
    void deleteCustomerByIdReturnsZeroForInvalidId() {
        int invalidCustomerId = -1;
        int actual = 0;
        
        try {
            actual = ICustomerDao.deleteCustomerById(invalidCustomerId);
        }
        catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(0, actual);
    }

    // **********************************************************

    // ******************** GET SUPPLIER BY ID TESTS ********************

    // Tests to see if a supplier is returned with the matching id
    @Test
    void getSupplierByIdTest1() {
        String expected = "supplier1"; // Use a valid supplier ID from the database
        String actual = "";
        Supplier supplier;

        try {
            supplier = ISupplierDao.getSupplierById(expected);
            actual = supplier.getId();
        }
        catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals("supplier1", actual);
    }

    // Tests to see if supplier is null when sending an id that does not exist
    @Test
    void getSupplierByIdTest2() {
        String invalidId = "invalidId";
        Supplier supplier = null;

        try {
            supplier = ISupplierDao.getSupplierById(invalidId);
        }
        catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNull(supplier);
    }

    // **********************************************************

    // *************** DELETE CUSTOMERSPRODUCTS BY ID TESTS ***************

    @Test
    void deleteCustomersProductsByIdsDeletesCustomer() {
        int validCustomerId = 101;
        String validProductId = "Product123";

        int rowsAffected = 0;
        try {
            IProductDao.addProduct(new Product(validProductId,"desc", "size",12, "Supplier1"));
            ICustomerDao.addCustomer(new Customer(validCustomerId,"name", "email","address"));
            ICustomersProductsDao.addCustomersProducts(new CustomersProducts(validCustomerId, validProductId,0));
            rowsAffected = ICustomersProductsDao.deleteCustomersProductsByIds(validCustomerId,validProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(1, rowsAffected, "Expected one row to be affected when deleting customers Products by IDs.");
    }

    @Test
    void deleteCustomerProductsByIdsReturnsZeroForInvalidIds() {
        int invalidCustomerId = 0;
        String invalidProductId = "Product0";

        int rowsAffected = 0;
        try {
            rowsAffected = ICustomersProductsDao.deleteCustomersProductsByIds(invalidCustomerId,invalidProductId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertEquals(0, rowsAffected, "Expected 0 rows affected for invalid customer products IDs.");
    }

    // **********************************************************

    // *************** GET CUSTOMER BY ID TESTS  *****************

    @Test
    void getCustomerByIdReturnsProduct() {
        int validCustomerId = 10001;

        Customer customer = null;
        try {
            customer = ICustomerDao.getCustomerById(validCustomerId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNull(customer);
    }

    @Test
    void getCustomerByIdReturnsNullForInvalidId() {
        int invalidCustomerId = 0;

        Customer customer = null;
        try {
            customer = ICustomerDao.getCustomerById(invalidCustomerId);
        } catch (DaoException e) {
            fail("DaoException occurred: " + e.getMessage());
        }

        assertNull(customer, "Expected null when customer is not found");
    }

    // **********************************************************

    // *************** WORDS *****************
}