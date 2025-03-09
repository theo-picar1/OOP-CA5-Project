package com.example.oopca5project.DAOs;

import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlProductDao extends MySqlDao implements ProductDaoInterface {
    @Override
    public List<Product> getAllProducts() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Product> productList = new ArrayList<>();

        try {
            connection = this.getConnection();
            String query = "SELECT * FROM Products";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String productId = resultSet.getString("product_id");
                String description = resultSet.getString("product_description");
                String size = resultSet.getString("size");
                double unitPrice = resultSet.getDouble("unit_price");
                String supplierId = resultSet.getString("supplier_id");

                Product product = new Product(productId, description, size, unitPrice, supplierId);
                productList.add(product);
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving products: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return productList;
    }

    @Override
    public Product getProductById(String productId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Product product = null;

        try {
            connection = this.getConnection();
            String query = "SELECT * FROM Products WHERE product_id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, productId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String description = resultSet.getString("product_description");
                String size = resultSet.getString("size");
                double unitPrice = resultSet.getDouble("unit_price");
                String supplierId = resultSet.getString("supplier_id");

                product = new Product(productId, description, size, unitPrice, supplierId);
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving product: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return product;
    }

    @Override
    public void deleteProductById(String productId) throws DaoException {
        String query = "DELETE FROM Products WHERE product_id = ?";
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error deleting product: " + e.getMessage()); // Fixed error message
        }
    }

    @Override
    public void addProduct(Product p) throws DaoException {
        // Initializing variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Get connection to database using MySqlDao method
            connection = this.getConnection();

            // Making query to add product
            String query = "INSERT INTO Products (Product_Id, Product_Description, Size, Unit_Price, Supplier_Id) VALUES (?, ?, ?, ?, ?)";

            // Making the query into a prepared statement
            preparedStatement = connection.prepareStatement(query);

            // Initializing/Setting '?' in the prepared statement
            preparedStatement.setString(1, p.getId());
            preparedStatement.setString(2, p.getDescription());
            preparedStatement.setString(3, p.getSize());
            preparedStatement.setDouble(4, p.getPrice());
            preparedStatement.setString(5, p.getSupplierId());

            // Getting the value of where the product was added
            int rowsInserted = preparedStatement.executeUpdate();

            // Checking if the value is bigger than 0 to confirm the product was added
            if (rowsInserted > 0) {
                System.out.println("Product added successfully!");
            } else {
                System.out.println("Product not added!");
            }

            // Catch SQLException
        } catch (SQLException e) {

            // Throws DaoException
            throw new DaoException("addProductResultSet() " + e.getMessage());

        } finally {

            try {

                // Closes prepared statement
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
    }

    @Override
    public void updateProduct(String id, Product p) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();

            // Query will be added in next commit
            String query = "";

            preparedStatement = connection.prepareStatement(query);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new DaoException("No rows were updated. Product might not exist.");
            }
        } catch (SQLException e) {
            throw new DaoException("updateProduct() " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("updateProduct() " + e.getMessage());
            }
        }
    }

//    @Override
//    public List<Player> findPlayersApplyFilter(playerAgeComparator) throws DaoException {
//        return null;
//    }

}
