package com.example.oopca5project.DAOs;

import java.util.List;

import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;

public interface ProductDaoInterface {
//  Feature 1 – Get all Products
//              e.g. List<Product> getAllProducts() -
//              return a List of all the Products and display the returned list.

    List<Product> getAllProducts() throws DaoException;

//  Feature 2 – Find and Display (a single) Product by Key
//              e.g. Product getProductById(id ) – return a single entity (DTO)
//              and display its contents.

    Product getProductById(String id) throws DaoException;

//  Feature 3 – Delete an Entity by key
//              e.g. deleteProductById(id) – remove specified Product from database

    int deleteProductById(String id) throws DaoException;

//  Feature 4 – Insert a Product (gather data, instantiate a Product object, pass
//              into DAO method for insertion in DB)
//              e.g. Product insertProduct(Product p)
//              return new Product (Product DTO) that includes the assigned auto-id.

    int addProduct(Product p) throws DaoException;

//  Feature 5 – Update an existing Product by ID using supplied Product object
//              e.g. updateProduct(int id, Product p) – executes specified updates

    int updateProduct(String id, Product p) throws DaoException;
}