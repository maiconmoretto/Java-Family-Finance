select * from family_finance.category;
SELECT * FROM family_finance.user;
SELECT * FROM family_finance.expenses;
INSERT INTO family_finance.user (name, email) VALUES ('maicon', 'maicon@maicon');
INSERT INTO family_finance.category (description, created_by) VALUES ('Lazer', 1);
INSERT INTO family_finance.expenses (description, created_by, category_id, value) 
VALUES ('gasto no motel', 1, 1, 3.33);

DROP SCHEMA family_finance; 
