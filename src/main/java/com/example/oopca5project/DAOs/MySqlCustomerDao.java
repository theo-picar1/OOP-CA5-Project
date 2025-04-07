package com.example.oopca5project.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.Exceptions.DaoException;

public class MySqlCustomerDao extends MySqlDao implements CustomerDaoInterface {

    @Override
    public List<Customer> getAllCustomers() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Customer> CustomerList = new ArrayList<>();

        try {
            connection = this.getConnection();

            String query = "SELECT * FROM Customers";
            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                String customerName = resultSet.getString("customer_name");
                String customerEmail = resultSet.getString("customer_email");
                String customerAddress = resultSet.getString("customer_address");

                Customer customer = new Customer(customerId, customerName, customerEmail, customerAddress);
                CustomerList.add(customer);
            }
        } catch (SQLException e) {
            throw new DaoException("getAllCustomers() error! " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return CustomerList;
    }

    @Override
    public int deleteCustomerById(int customerId) throws DaoException {
        Connection connection = null;
        PreparedStatement deleteFromCustomersProducts = null;
        PreparedStatement deleteFromCustomers = null;
        int rowsAffected = 0;

        try {
            connection = this.getConnection();

            // Delete from CustomersProducts first due to foreign key constraint
            String deleteProductsQuery = "DELETE FROM CustomersProducts WHERE customer_id = ?";
            deleteFromCustomersProducts = connection.prepareStatement(deleteProductsQuery);
            deleteFromCustomersProducts.setInt(1, customerId);
            deleteFromCustomersProducts.executeUpdate();

            // Now delete from Customers
            String deleteCustomerQuery = "DELETE FROM Customers WHERE customer_id = ?";
            deleteFromCustomers = connection.prepareStatement(deleteCustomerQuery);
            deleteFromCustomers.setInt(1, customerId);
            rowsAffected = deleteFromCustomers.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("deleteCustomerById() error! " + e.getMessage());
        } finally {
            try {
                if (deleteFromCustomersProducts != null) {
                    deleteFromCustomersProducts.close();
                }
                if (deleteFromCustomers != null) {
                    deleteFromCustomers.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("deleteCustomerById() error closing resources! " + e.getMessage());
            }
        }

        return rowsAffected;
    }

    @Override
    public Customer getCustomerById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Customer customer = null;

        try {
            connection = this.getConnection();
            String query = "SELECT * FROM customers WHERE customer_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("customer_name");
                String address = resultSet.getString("customer_address");
                String email = resultSet.getString("customer_email");

                customer = new Customer(id, name, email, address);
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving product: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return customer;
    }

    @Override
    public int addCustomer(Customer c) throws DaoException {
        // Initializing variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected;

        try {
            // Get connection to database using MySqlDao method
            connection = this.getConnection();

            if (c != null) {
                Customer product = getCustomerById(c.getId());
                if (product == null) {

                    // Making query to add product
                    String query = "INSERT INTO Customers VALUES (?, ?, ?, ?)";
                    
                    // Making the query into a prepared preparedStatement
                    preparedStatement = connection.prepareStatement(query);

                    // Initializing/Setting '?' in the prepared preparedStatement
                    preparedStatement.setInt(1, c.getId());
                    preparedStatement.setString(2, c.getName());
                    preparedStatement.setString(3, c.getEmail());
                    preparedStatement.setString(4, c.getAddress());

                    // Getting the value of how many rows were affected
                    rowsAffected = preparedStatement.executeUpdate();
                } else {
                    throw new DaoException("addProduct() error! " + "Product already exists!");
                }
            } else {
                return 0;
            }
        } catch (SQLException e) {
            return 0;

        } finally {
            try {
                // Closes prepared preparedStatement
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                // Frees up connection
                if (connection != null) {
                    freeConnection(connection);
                }
                // Catches SQLException
            } catch (SQLException e) {
                // Throws DaoException
                throw new DaoException("addProduct() " + e.getMessage());

            }
        }

        return rowsAffected;
    }
}
