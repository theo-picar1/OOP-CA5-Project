drop database if exists Paper_company;
create database Paper_company;
use Paper_company;

drop table if exists Products;

create table Products
(
    product_id          varchar(20)  not null,
    product_description varchar(255) not null,
    size                varchar(50)  not null,
    unit_price          double not null,
    supplier_id         varchar(30)  not null,
    primary key (product_id)
);

CREATE TABLE Suppliers
(
    supplier_id   INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(255) NOT NULL,
    contact_name  VARCHAR(255),
    contact_email VARCHAR(255),
    phone_number  VARCHAR(20),
    address       VARCHAR(255),
    city          VARCHAR(100),
    postal_code   VARCHAR(20),
    country       VARCHAR(100)
);


INSERT INTO Products(product_id, product_description, size, unit_price, supplier_id)
VALUES ('product1', 'A4 Paper', '20cm x 50cm', 6.29, 'Supplier_1'),
       ('product2', 'A3 Paper', '30cm x 60cm', 9.29, 'Supplier_2'),
       ('product3', 'A5 Paper', '15cm x 30cm', 4.29, 'Supplier_3'),
       ('product4', 'A4 Hardback Copy', '20cm x 50cm', 6.50, 'Supplier_4'),
       ('product5', 'Refill Pad', '20cm x 50cm', 9.99, 'Supplier_5'),
       ('product6', 'A2 Paper', '40cm x 80cm', 15.99, 'Supplier_6'),
       ('product7', 'Notebook', '18cm x 25cm', 5.49, 'Supplier_7'),
       ('product8', 'Sketch Pad', '30cm x 40cm', 12.99, 'Supplier_8'),
       ('product9', 'Graph Paper Pad', '20cm x 50cm', 7.89, 'Supplier_9'),
       ('product10', 'Legal Pad', '22cm x 35cm', 6.75, 'Supplier_10');

INSERT INTO Suppliers (supplier_name, contact_name, contact_email, phone_number, address, city, postal_code, country)
VALUES
    ('ABC Supplies', 'John Doe', 'johndoe@abc.com', '123-456-7890', '123 ABC St.', 'New York', '10001', 'USA'),
    ('XYZ Ltd.', 'Jane Smith', 'janesmith@xyz.com', '987-654-3210', '456 XYZ Ave.', 'Los Angeles', '90001', 'USA'),
    ('Global Corp', 'Samuel Green', 'samuelgreen@global.com', '555-123-4567', '789 Global Rd.', 'Chicago', '60601', 'USA'),
    ('Tech Solutions', 'Michael Brown', 'michaelbrown@tech.com', '555-234-5678', '321 Tech Ln.', 'San Francisco', '94101', 'USA'),
    ('Eco Suppliers', 'Sarah White', 'sarahwhite@eco.com', '555-345-6789', '654 Eco Dr.', 'Miami', '33101', 'USA'),
    ('Quality Goods', 'William Black', 'williamblack@quality.com', '555-456-7890', '987 Quality Blvd.', 'Houston', '77001', 'USA'),
    ('Prime Goods', 'Emma Blue', 'emmablue@prime.com', '555-567-8901', '159 Prime Pkwy', 'Seattle', '98101', 'USA'),
    ('Fast Supplies', 'Olivia Green', 'oliviagreen@fast.com', '555-678-9012', '753 Fast St.', 'Dallas', '75201', 'USA'),
    ('Metro Goods', 'Lucas Gray', 'lucasgray@metro.com', '555-789-0123', '258 Metro Rd.', 'Boston', '02101', 'USA'),
    ('Rapid Corp', 'Ava Red', 'avared@rapid.com', '555-890-1234', '369 Rapid St.', 'Denver', '80201', 'USA');
