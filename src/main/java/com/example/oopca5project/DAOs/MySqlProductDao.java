package com.example.oopca5project.DAOs;

import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.DTOs.Supplier;
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Product> productList = new ArrayList<>();

        try {
            connection = this.getConnection();

            String query = "SELECT * FROM Products";
            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

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
            throw new DaoException("getAllProducts() error! " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Product product = null;

        try {
            connection = this.getConnection();
            String query = "SELECT * FROM Products WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, productId);

            resultSet = preparedStatement.executeQuery();

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
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return product;
    }

    @Override
    public int deleteProductById(String productId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected;

        try {
            connection = this.getConnection();

            String deleteQuery = "DELETE FROM Products WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);

            preparedStatement.setString(1, productId);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("deleteProductById() error! " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("deleteProductById() error!" + e.getMessage());
            }
        }

        return rowsAffected;
    }

    @Override
    public int addProduct(Product p) throws DaoException {
        // Initializing variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected;

        try {
            // Get connection to database using MySqlDao method
            connection = this.getConnection();

            if (p != null) {
                Product product = getProductById(p.getId());
                if (product == null) {

                    // Making query to add product
                    String query = "INSERT INTO Products VALUES (?, ?, ?, ?, ?)";
                    // Making the query into a prepared preparedStatement
                    preparedStatement = connection.prepareStatement(query);

                    // Initializing/Setting '?' in the prepared preparedStatement
                    preparedStatement.setString(1, p.getId());
                    preparedStatement.setString(2, p.getDescription());
                    preparedStatement.setString(3, p.getSize());
                    preparedStatement.setDouble(4, p.getPrice());
                    preparedStatement.setString(5, p.getSupplierId());

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

    @Override
    public int updateProduct(String id, Product p) throws DaoException {
        // Initializing variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected;

        try {
            if(p != null) {
                // Get connection to database using MySqlDao method
                connection = this.getConnection();

                // Making query to update product
                String query = "UPDATE Products SET Product_id = ?, Product_Description = ?, Size = ?, Unit_Price = ?, Supplier_id = ? WHERE Product_id = ?";

                // Making the query into a prepared preparedStatement
                preparedStatement = connection.prepareStatement(query);

                // Initializing/Setting '?' in the prepared preparedStatement
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, p.getDescription());
                preparedStatement.setString(3, p.getSize());
                preparedStatement.setDouble(4, p.getPrice());
                preparedStatement.setString(5, p.getSupplierId());
                preparedStatement.setString(6, id);

                // Getting the value of how many rows were affected
                rowsAffected = preparedStatement.executeUpdate();
            }else {
                return 0;
            }
        } catch (SQLException e) {
            // Throws DaoException
            throw new DaoException("updateProductResultSet() " + e.getMessage());

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
                throw new DaoException("updateProduct() " + e.getMessage());

            }
        }

        return rowsAffected;
    }
}
