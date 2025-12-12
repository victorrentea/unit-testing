-- add a unique constraint on the supplier code
ALTER TABLE supplier ADD CONSTRAINT supplier_code_unique UNIQUE (code);