package com.example.oopca5project.DAOs;

import java.util.List;

import com.example.oopca5project.DTOs.CustomersProducts;
import com.example.oopca5project.Exceptions.DaoException;

public interface CustomersProductsDaoInterface {
    List<CustomersProducts> getAllCustomerProducts() throws DaoException;

    CustomersProducts getCustomersProductsByIds(int customerId, String productId) throws DaoException;

    int updateCustomersProducts(int customerID, String productId, CustomersProducts cp) throws DaoException;

    int addCustomersProducts(CustomersProducts cp) throws DaoException;
}