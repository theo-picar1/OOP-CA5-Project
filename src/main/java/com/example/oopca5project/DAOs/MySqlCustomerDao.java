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
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new DaoException("Error closing resources: " + e.getMessage());
            }
        }
        return CustomerList;
    }
}
