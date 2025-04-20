package com.example.oopca5project.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.oopca5project.DTOs.CustomersProducts;
import com.example.oopca5project.Exceptions.DaoException;

public class MySqlCustomersProductsDao extends MySqlDao implements CustomersProductsDaoInterface {

    @Override
    public List<CustomersProducts> getAllCustomerProducts() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<CustomersProducts> customerProductList = new ArrayList<>();

        try {
            connection = this.getConnection();

            String query = "SELECT cp.customer_id, cp.product_id, cp.quantity " +
                    "FROM CustomersProducts cp " +
                    "JOIN Customers c ON cp.customer_id = c.customer_id " +
                    "JOIN Products p ON cp.product_id = p.product_id";
            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                String productId = resultSet.getString("product_id");
                int quantity = resultSet.getInt("quantity");

                // Only set customerId, productId, and quantity as per the DTO
                CustomersProducts customerProduct = new CustomersProducts();
                customerProduct.setCustomerId(customerId);
                customerProduct.setProductId(productId);
                customerProduct.setQuantity(quantity);

                customerProductList.add(customerProduct);
            }
        } catch (SQLException e) {
            throw new DaoException("getAllCustomerProducts() error! " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return customerProductList;
    }

    @Override
    public CustomersProducts getCustomersProductsByIds(int customerID, String productID) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        CustomersProducts CustomerP = null;

        try {
            connection = this.getConnection();

            String query = "SELECT * FROM CustomersProducts WHERE Customer_id = ? AND product_id = ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerID);
            preparedStatement.setString(2, productID);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");

                CustomerP = new CustomersProducts(customerID, productID, quantity);
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving CustomersProduct: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return CustomerP;
    }

    @Override
    public int updateCustomersProducts(int customerID, String productID, CustomersProducts cp) throws DaoException {
        // Initializing variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected;

        try {
            if(cp != null) {
                // Get connection to database using MySqlDao method
                connection = this.getConnection();

                // Making query to update CustomersProducts
                String query = "UPDATE CustomersProducts SET quantity = ? WHERE customer_ID = ? AND product_ID = ?";

                // Making the query into a prepared preparedStatement
                preparedStatement = connection.prepareStatement(query);

                // Initializing/Setting '?' in the prepared preparedStatement
                preparedStatement.setInt(1, cp.getQuantity());
                preparedStatement.setInt(2, customerID);
                preparedStatement.setString(3, productID);

                // Getting the value of how many rows were affected
                rowsAffected = preparedStatement.executeUpdate();
            }else {
                return 0;
            }
        } catch (SQLException e) {
            // Throws DaoException
            throw new DaoException("updateCustomersProductsByCustomerIdResultSet() " + e.getMessage());

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
                throw new DaoException("updateCustomersProductsByCustomerId() " + e.getMessage());

            }
        }

        return rowsAffected;
    }

    @Override
    public int addCustomersProducts(CustomersProducts cp) throws DaoException {
        // Initializing variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected;

        try {
            // Get connection to database using MySqlDao method
            connection = this.getConnection();

            if (cp != null) {
                String query = "INSERT INTO CustomersProducts VALUES (?, ?, ?)";
                preparedStatement = connection.prepareStatement(query);

                // Initializing/Setting '?' in the prepared preparedStatement
                preparedStatement.setInt(1, cp.getCustomerId());
                preparedStatement.setString(2, cp.getProductId());
                preparedStatement.setInt(3, cp.getQuantity());

                // Getting the value of how many rows were affected
                rowsAffected = preparedStatement.executeUpdate();
            }
            else {
                throw new DaoException("addCustomersProducts() error! " + "Customers Products already exists!");
            }
        } catch (SQLException e) {
            // Throws DaoException
            throw new DaoException("addCustomersProductsResultSet() " + e.getMessage());

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
                throw new DaoException("addCustomersProducts() " + e.getMessage());
            }
        }
        return rowsAffected;
    }

    @Override
    public int deleteCustomersProductsByIds(int customerId, String productId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected;

        try {
            connection = this.getConnection();

            String deleteQuery = "DELETE FROM CustomersProducts WHERE product_id = ? AND customer_id = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);

            preparedStatement.setString(1, productId);
            preparedStatement.setInt(2, customerId);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("deleteCustomersProductsByIds() error! " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("deleteCustomersProductsByIds() error!" + e.getMessage());
            }
        }

        return rowsAffected;
    }
}
