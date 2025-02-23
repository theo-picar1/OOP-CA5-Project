drop database if exists Paper_company;
create database Paper_company;
use Paper_company;

drop table if exists Products;

create table Products (
    product_id varchar(20) not null,
    product_description varchar(255) not null,
    size varchar(50) not null,
    unit_price double not null,
    supplier_id varchar(30) not null,
    primary key (product_id)
);

INSERT INTO Products(product_id, product_description, size, unit_price, supplier_id) VALUES
('product1', 'A4 Paper', '20cm x 50cm', 6.29, 'Supplier_1'),
('product2', 'A3 Paper', '30cm x 60cm', 9.29, 'Supplier_2'),
('product3', 'A5 Paper', '15cm x 30cm', 4.29, 'Supplier_3'),
('product4', 'A4 Hardback Copy', '20cm x 50cm', 6.50, 'Supplier_4'),
('product5', 'Refill Pad', '20cm x 50cm', 9.99, 'Supplier_5'),
('product6', 'A2 Paper', '40cm x 80cm', 15.99, 'Supplier_6'),
('product7', 'Notebook', '18cm x 25cm', 5.49, 'Supplier_7'),
('product8', 'Sketch Pad', '30cm x 40cm', 12.99, 'Supplier_8'),
('product9', 'Graph Paper Pad', '20cm x 50cm', 7.89, 'Supplier_9'),
('product10', 'Legal Pad', '22cm x 35cm', 6.75, 'Supplier_10');
