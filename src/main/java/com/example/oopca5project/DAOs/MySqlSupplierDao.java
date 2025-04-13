package com.example.oopca5project.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.Exceptions.DaoException;

public class MySqlSupplierDao extends MySqlDao implements SupplierDaoInterface {
    @Override
    public List<Supplier> getAllSuppliers() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Supplier> supplierList = new ArrayList<>();

        try {
            connection = this.getConnection();

            String query = "SELECT * FROM Suppliers";
            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String supplierId = resultSet.getString("supplier_id");
                String supplierName = resultSet.getString("supplier_name");
                String supplierPhoneNo = resultSet.getString("supplier_phone_no");
                String supplierEmail = resultSet.getString("supplier_email");

                Supplier supplier = new Supplier(supplierId, supplierName, supplierPhoneNo, supplierEmail);
                supplierList.add(supplier);
            }
        } catch (SQLException e) {
            throw new DaoException("getAllSuppliers() error! " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return supplierList;
    }

    @Override
    public Supplier getSupplierByProductId(String productId) throws DaoException {
        // Initializing variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Supplier supplier = null;

        try {

            // Get connection
            connection = this.getConnection();

            // Set up SQL query
            String query = "SELECT * FROM Suppliers WHERE supplier_id = (SELECT supplier_id FROM Products WHERE product_id = ?);";

            // set up preparedStatement
            preparedStatement = connection.prepareStatement(query);

            // Initialize variable in preparedStatement
            preparedStatement.setString(1, productId);

            // Execute query
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                
                // Get variables
                String supplierId = resultSet.getString("supplier_Id");
                String supplierName = resultSet.getString("supplier_Name");
                String supplierPhoneNo = resultSet.getString("supplier_phone_no");
                String supplierEmail = resultSet.getString("supplier_email");

                // Set up Supplier object
                supplier = new Supplier(supplierId, supplierName, supplierPhoneNo, supplierEmail);
            } else {
                System.out.println("\nNo supplier found for productId: " + productId + "\n");
            }

            // catch Exceptions
        } catch (SQLException e) {

            // Throw DaoException
            throw new DaoException("Error retrieving Supplier: " + e.getMessage());
        } finally {

            // close all resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }

        // return Supplier object
        return supplier;
    }

    @Override
    public int addSupplier(Supplier supplier) throws DaoException {
        // Initializing variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected;

        try {
            // Get connection to database using MySqlDao method
            connection = this.getConnection();

            if (supplier != null) {
                String query = "INSERT INTO Suppliers VALUES (?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(query);

                // Initializing/Setting '?' in the prepared preparedStatement
                preparedStatement.setString(1, supplier.getId());
                preparedStatement.setString(2, supplier.getName());
                preparedStatement.setString(3, supplier.getPhoneNo());
                preparedStatement.setString(4, supplier.getEmail());

                // Getting the value of how many rows were affected
                rowsAffected = preparedStatement.executeUpdate();
            }
            else {
                throw new DaoException("addProduct() error! " + "Product already exists!");
            }
        } catch (SQLException e) {
            // Throws DaoException
            throw new DaoException("addProductResultSet() " + e.getMessage());

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
    public Supplier getSupplierById(String supplierId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Supplier supplier = null;

        try {
            connection = this.getConnection();
            String query = "SELECT * FROM Suppliers WHERE supplier_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, supplierId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("supplier_name");
                String phone_no = resultSet.getString("supplier_phone_no");
                String email = resultSet.getString("supplier_email");

                supplier = new Supplier(supplierId, name, phone_no, email);
            }
        }
        catch (SQLException e) {
            throw new DaoException("Error retrieving product: " + e.getMessage());
        }
        finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return supplier;
    }

    @Override
    public int deleteSupplierById(String supplierId) throws DaoException {
        Connection connection = null;
        PreparedStatement deleteProductsStmt = null;
        PreparedStatement deleteSupplierStmt = null;
        int rowsAffected;

        try {
            // Get connection
            connection = this.getConnection();

            // Delete all products by the supplier
            String deleteProductsQuery = "DELETE FROM Products WHERE supplier_id = ?";
            deleteProductsStmt = connection.prepareStatement(deleteProductsQuery);
            deleteProductsStmt.setString(1, supplierId);
            deleteProductsStmt.executeUpdate();

            // Delete the supplier
            String deleteSupplierQuery = "DELETE FROM Suppliers WHERE supplier_id = ?";
            deleteSupplierStmt = connection.prepareStatement(deleteSupplierQuery);
            deleteSupplierStmt.setString(1, supplierId);
            rowsAffected = deleteSupplierStmt.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("deleteSupplierById() error! " + e.getMessage());
        } finally {
            try {
                if (deleteProductsStmt != null) deleteProductsStmt.close();
                if (deleteSupplierStmt != null) deleteSupplierStmt.close();
                if (connection != null) freeConnection(connection);
            } catch (SQLException e) {
                throw new DaoException("deleteSupplierById() error closing resources! " + e.getMessage());
            }
        }

        return rowsAffected;
    }

    @Override
    public int updateSupplier(String id, Supplier s) throws DaoException {
        // Initializing variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected;

        try {
            if(s != null) {
                // Get connection to database using MySqlDao method
                connection = this.getConnection();

                // Making query to update supplier
                String query = "UPDATE suppliers SET supplier_name = ?, supplier_phone_no = ?, supplier_email = ? WHERE Supplier_id = ?";

                // Making the query into a prepared preparedStatement
                preparedStatement = connection.prepareStatement(query);

                // Initializing/Setting '?' in the prepared preparedStatement
                preparedStatement.setString(1, s.getName());
                preparedStatement.setString(2, s.getPhoneNo());
                preparedStatement.setString(3, s.getEmail());
                preparedStatement.setString(4, id);

                // Getting the value of how many rows were affected
                rowsAffected = preparedStatement.executeUpdate();
            }else {
                return 0;
            }
        } catch (SQLException e) {
            // Throws DaoException
            throw new DaoException("updateSupplierResultSet() " + e.getMessage());

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
                throw new DaoException("updateSupplier() " + e.getMessage());

            }
        }

        return rowsAffected;
    }
}
