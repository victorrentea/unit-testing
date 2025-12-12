-- Use a fixed ID to avoid sequence dialect issues in H2
INSERT INTO supplier(id, active, name, code)
VALUES (1000, TRUE, 'Test Supplier Lower', 'abc_test');
