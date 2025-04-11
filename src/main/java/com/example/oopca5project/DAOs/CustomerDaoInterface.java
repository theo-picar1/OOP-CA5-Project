package com.example.oopca5project.DAOs;

import java.util.List;

import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.Exceptions.DaoException;

public interface CustomerDaoInterface {

    List<Customer> getAllCustomers() throws DaoException;

    int addCustomer(Customer c) throws DaoException;

    Customer getCustomerById(int id) throws DaoException;

    int deleteCustomerById(int customerId) throws DaoException;

    int updateCustomer(int id, Customer c) throws DaoException;
}