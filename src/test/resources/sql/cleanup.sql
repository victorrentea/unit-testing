-- in the order of FK
DELETE FROM PRODUCT;
DELETE FROM SUPPLIER;

-- sequences are not rolledback by @Transcational
ALTER SEQUENCE product_seq RESTART WITH 1;
ALTER SEQUENCE supplier_seq RESTART WITH 1;
