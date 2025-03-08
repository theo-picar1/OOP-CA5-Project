package DAOs;

import DTOs.Product;
import Exceptions.DaoException;

import java.util.List;

public interface ProductDaoInterface {
//  Feature 1 – Get all Products
//              e.g. List<Product> getAllProducts() -
//              return a List of all the Products and display the returned list.

    public List<Product> getAllProducts() throws DaoException;

//  Feature 2 – Find and Display (a single) Product by Key
//              e.g. Product getProductById(id ) – return a single entity (DTO)
//              and display its contents.

    public Product getProductById(int id) throws DaoException;

//  Feature 3 – Delete an Entity by key
//              e.g. deleteProductById(id) – remove specified Product from database

    public void deleteProductById(int id) throws DaoException;

//  Feature 4 – Insert a Product (gather data, instantiate a Product object, pass
//              into DAO method for insertion in DB)
//              e.g. Product insertProduct(Product p)
//              return new Product (Product DTO) that includes the assigned auto-id.

    public void addProduct(Product p) throws DaoException;

//  Feature 5 – Update an existing Product by ID using supplied Product object
//              e.g. updateProduct(int id, Product p) – executes specified updates

    public void updateProduct(int id, Product p) throws DaoException;

//  Feature 6 – Get list of entities matching a filter (based on DTO object)
//              e.g. List<Product> findProductApplyFilter( ProductAgeComparator)

    //public List<Product> findPlayersApplyFilter(ProductPriceComparator) throws DaoException;
}