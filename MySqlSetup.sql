drop database if exists Paper_company;
create database Paper_company;
use Paper_company;

drop table if exists Products;
drop table if exists Suppliers;
drop table if exists Customers;
drop table if exists CustomersProducts;

create table Suppliers
(
    supplier_id varchar(30) not null,
    supplier_name varchar(20),
    supplier_phone_no varchar(50),
    supplier_email varchar(320),
    primary key (supplier_id)
);

create table Products
(
    product_id varchar(20) not null,
    product_description varchar(255) not null,
    size varchar(50)  not null,
    unit_price double not null,
    supplier_id varchar(30)  not null,
    primary key (product_id),
    foreign key (supplier_id) references Suppliers(supplier_id)
);

create table Customers (
    customer_id int not null auto_increment,
    customer_name varchar(20),
    customer_email varchar(320),
    customer_address varchar(50),
    primary key (customer_id)
);

create table CustomersProducts (
    customer_id int not null,
    product_id varchar(20) not null,
    quantity int not null,
    foreign key (customer_id) references Customers(customer_id),
    foreign key (product_id) references Products(product_id)
);

insert into Suppliers(supplier_id, supplier_name, supplier_phone_no, supplier_email) values
    ('Supplier1', 'Theo', '0842641245', 'supplier1@gmail.com'),
    ('Supplier2', 'Ewan', '0852321298', 'supplier2@icloud.com'),
    ('Supplier3', 'Oisin', '0859867465', 'supplier1@yahoo.com'),
    ('Supplier4', 'Dami', '0855253415', 'supplier1@me.com'),
    ('Supplier5', 'Lala', '0899861276', 'supplier1@outlook.com');

INSERT INTO Products(product_id, product_description, size, unit_price, supplier_id) VALUES
    ('product1', 'A4 Paper', '20cm x 50cm', 6.29, 'Supplier1'),
    ('product2', 'A3 Paper', '30cm x 60cm', 9.29, 'Supplier2'),
    ('product3', 'A5 Paper', '15cm x 30cm', 4.29, 'Supplier3'),
    ('product4', 'A4 Hardback Copy', '20cm x 50cm', 6.50, 'Supplier4'),
    ('product5', 'Refill Pad', '20cm x 50cm', 9.99, 'Supplier5'),
    ('product6', 'A2 Paper', '40cm x 80cm', 15.99, 'Supplier1'),
    ('product7', 'Notebook', '18cm x 25cm', 5.49, 'Supplier2'),
    ('product8', 'Sketch Pad', '30cm x 40cm', 12.99, 'Supplier3'),
    ('product9', 'Graph Paper Pad', '20cm x 50cm', 7.89, 'Supplier4'),
    ('product10', 'Legal Pad', '22cm x 35cm', 6.75, 'Supplier5');

insert into Customers(customer_id, customer_name, customer_email, customer_address) values
    (10001, 'Chad', 'chad@yahoo.com', '31 High Street'),
    (10002, 'Richard', 'richARdP@gmail.com', '39 Down Street'),
    (10003, 'Eoghan', '30ghan@outlook.com', '23 Park Ave'),
    (10004, 'Eoin', '301n@me.com', '12 Park Grange'),
    (10005, 'Owen', 'oWEn@yahoo.com', '05 High Grange');

insert into CustomersProducts(customer_id, product_id, quantity) values
    (10001, 'product1', 2),
    (10001, 'product6', 10),
    (10002, 'product10', 4),
    (10001, 'product5', 2),
    (10004, 'product7', 10),
    (10003, 'product3', 2),
    (10003, 'product4', 14),
    (10005, 'product1', 5),
    (10005, 'product10', 4),
    (10005, 'product4', 18),
    (10004, 'product8', 32),
    (10004, 'product9', 19);