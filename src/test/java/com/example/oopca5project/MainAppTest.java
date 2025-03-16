package com.example.oopca5project;

import com.example.oopca5project.DTOs.Product;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class MainAppTest {

    // Question 6
    @Test
    // Tests to see if filteredProducts will return nothing if provided with no products to filter by
    void filterProductsTest1() {
        double price = 10.00;
        List<Product> products = new ArrayList<>();
        List<Product> filteredProducts = Methods.filterProductsByPrice(price, products);

        int expected = 0;
        int actual = filteredProducts.size();

        assertEquals(expected, actual);
    }

    @Test
    // Tests to see if filteredProducts will return everything in the provided products list
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
}