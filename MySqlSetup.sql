drop
database if exists Paper_company;
create
database Paper_company;
use
Paper_company;

drop table if exists Products;
drop table if exists Suppliers;

create table Products
(
    product_id          varchar(20)  not null,
    product_description varchar(255) not null,
    size                varchar(50)  not null,
    unit_price double not null,
    supplier_id         varchar(30)  not null,
    primary key (product_id)
);

create table Suppliers
(
    supplier_id       varchar(30) not null,
    supplier_name     varchar(20),
    supplier_phone_no varchar(50),
    supplier_email    varchar(320),
    primary key (supplier_id)
);


INSERT INTO Products(product_id, product_description, size, unit_price, supplier_id)
VALUES ('product1', 'A4 Paper', '20cm x 50cm', 6.29, 'Supplier1'),
       ('product2', 'A3 Paper', '30cm x 60cm', 9.29, 'Supplier2'),
       ('product3', 'A5 Paper', '15cm x 30cm', 4.29, 'Supplier3'),
       ('product4', 'A4 Hardback Copy', '20cm x 50cm', 6.50, 'Supplier4'),
       ('product5', 'Refill Pad', '20cm x 50cm', 9.99, 'Supplier5'),
       ('product6', 'A2 Paper', '40cm x 80cm', 15.99, 'Supplier1'),
       ('product7', 'Notebook', '18cm x 25cm', 5.49, 'Supplier2'),
       ('product8', 'Sketch Pad', '30cm x 40cm', 12.99, 'Supplier3'),
       ('product9', 'Graph Paper Pad', '20cm x 50cm', 7.89, 'Supplier4'),
       ('product10', 'Legal Pad', '22cm x 35cm', 6.75, 'Supplier5');

insert into Suppliers(supplier_id, supplier_name, supplier_phone_no, supplier_email)
values ('Supplier1', 'Theo', '0842641245', 'supplier1@gmail.com'),
       ('Supplier2', 'Ewan', '0852321298', 'supplier2@icloud.com'),
       ('Supplier3', 'Oisin', '0859867465', 'supplier1@yahoo.com'),
       ('Supplier4', 'Dami', '0855253415', 'supplier1@me.com'),
       ('Supplier5', 'Lala', '0899861276', 'supplier1@outlook.com');