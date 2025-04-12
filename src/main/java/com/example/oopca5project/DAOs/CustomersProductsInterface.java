package com.example.oopca5project.DAOs;

import com.example.oopca5project.DTOs.CustomersProducts;
import com.example.oopca5project.Exceptions.DaoException;

import java.util.List;

public interface CustomersProductsInterface {
    List<CustomersProducts> getAllCustomerProducts() throws DaoException;
}
