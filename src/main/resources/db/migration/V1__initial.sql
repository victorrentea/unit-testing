create sequence hibernate_sequence start 1 increment 1;

create table product (id int8 not null, barcode varchar(255), category int4, create_date timestamp, name varchar(255), supplier_id int8, primary key (id));

create table supplier (id int8 not null, active boolean not null, name varchar(255), primary key (id));

