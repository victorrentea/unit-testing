-- in the order of FK -- disable/enable constraints ; truncate
DELETE FROM PRODUCT;
DELETE FROM SUPPLIER;

-- sequences are not rolledback by @Transcational
ALTER SEQUENCE hibernate_sequence RESTART WITH 1;

-- maybe reinsert back the HOLY data