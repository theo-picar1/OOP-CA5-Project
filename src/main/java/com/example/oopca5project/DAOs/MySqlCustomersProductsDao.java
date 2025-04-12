package com.example.oopca5project.DAOs;

import com.example.oopca5project.DTOs.CustomersProducts;
import com.example.oopca5project.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlCustomersProductsDao extends MySqlDao implements CustomersProductsInterface {

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
}
