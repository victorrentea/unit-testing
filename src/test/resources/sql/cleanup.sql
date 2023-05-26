-- in the order of FK
DELETE FROM PRODUCT;
DELETE FROM SUPPLIER;

-- sequences are not rolledback by @Transcational
ALTER SEQUENCE hibernate_sequence RESTART WITH 1;
