-- in the order of FK
DELETE FROM PRODUCT;
DELETE FROM SUPPLIER;

-- sequences are not rolledback by @Transcational
ALTER SEQUENCE hibernate_sequence RESTART WITH 1;

INSERT INTO SUPPLIER (id, name, code, active) VALUES (1, 'Supplier Name', 'S', 1);