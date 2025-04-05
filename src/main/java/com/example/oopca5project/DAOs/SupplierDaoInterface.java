package com.example.oopca5project.DAOs;

import java.util.List;

import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.Exceptions.DaoException;

public interface SupplierDaoInterface {

    List<Supplier> getAllSuppliers() throws DaoException;

    Supplier getSupplierByProductId(String supplierId) throws DaoException;

    Supplier getSupplierById(String id) throws DaoException;

    int addSupplier(Supplier s) throws DaoException;
}