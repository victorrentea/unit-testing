-- in the order of FK
DELETE FROM PRODUCT;
DELETE FROM SUPPLIER;

-- 500 more tables
-- insert some static COUNTRIES

-- sequences are not rolledback by @Transcational
ALTER SEQUENCE hibernate_sequence RESTART WITH 1;
