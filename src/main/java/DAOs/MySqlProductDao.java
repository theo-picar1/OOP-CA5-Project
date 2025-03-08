package DAOs;

import DTOs.Product;
import Exceptions.DaoException;
import DAOs.MySqlDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySqlProductDao extends MySqlDao implements ProductDaoInterface{
    @Override
    public List<Product> getAllProducts() throws DaoException {
        return null;
    }

    @Override
    public Product getProductById(int id) throws DaoException {
        return null;
    }

    @Override
    public void deleteProductById(int id) throws DaoException {

    }

    @Override
    public void addProduct(Product p) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();

            String query = "INSERT INTO Products (ProductId, ProductDescription, Size, UnitPrice, SupplierId) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);

            String ProductId = p.getId();
            String ProductDescription = p.getDescription();
            String Size = p.getSize();
            double unitPrice = p.getPrice();
            String supplierId = p.getSupplierId();

            preparedStatement.setString(1, ProductId);
            preparedStatement.setString(2, ProductDescription);
            preparedStatement.setString(3, Size);
            preparedStatement.setDouble(4, unitPrice);
            preparedStatement.setString(5, supplierId);

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Product added successfully!");
            } else {
                System.out.println("Product not added!");
            }
        } catch (SQLException e) {
            throw new DaoException("addProductResultSet() " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("addProduct() " + e.getMessage());
            }
        }
    }

    @Override
    public void updateProduct(int id, Product p) throws DaoException {

    }

//    @Override
//    public List<Player> findPlayersApplyFilter(playerAgeComparator) throws DaoException {
//        return null;
//    }

}
