create sequence supplier_seq start with 1 increment 50;

create table product (
    id int8 not null,
    barcode varchar(255),
    category varchar(255),
    created_date date,
    name varchar(255),
    created_by varchar(255),
    supplier_id int8,
    primary key (id));

create sequence product_seq start with 1 increment 50;

create table supplier (
    id int8 not null,
    active boolean not null default true,
    name varchar(255),
    primary key (id));

