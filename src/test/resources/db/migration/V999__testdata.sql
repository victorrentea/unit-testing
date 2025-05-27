
insert into supplier (id, active, code, name) values (1, true, 'S', 'Supplier');

alter sequence supplier_seq restart with 2; -- because of the above
