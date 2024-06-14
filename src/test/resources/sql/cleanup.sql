-- in the order of FK
DELETE FROM PRODUCT;
DELETE FROM SUPPLIER /*on CASCADE delete*/;
-- INSERT INTO SUPPLIER (ID, NAME) VALUES (1, 'S');
-- sequences are not rolledback by @Transcational
ALTER SEQUENCE hibernate_sequence RESTART WITH 1;


-- date de referinta (statice)
-- INSERT INTO COUNTRY (ID, NAME) VALUES (1, 'USA');
-- 'enum'-uri pe stilu vechi
-- INSERT INTO PRODUCT_STATUS (ID, NAME) VALUES (1, 'ACTIVE');
-- INSERT INTO PRODUCT_STATUS (ID, NAME) VALUES (2, 'INACTIVE');
-- INSERT INTO PRODUCT_STATUS (ID, NAME) VALUES (3, 'DELETED');
