select * from family_finance.category;
SELECT * FROM family_finance.user;
SELECT * FROM family_finance.expenses;
INSERT INTO family_finance.user (name, email) VALUES ('maicon', 'maicon@maicon');
INSERT INTO family_finance.category (description, created_by) VALUES ('Lazer', 2);
INSERT INTO family_finance.expenses (description, created_by, category_id, value) 
VALUES ('gasto no motel', 1, 1, 3.33);


SELECT c.description, e.id FROM family_finance.expenses e JOIN family_finance.category c;

SELECT 
expense.id, expense.description as description, expense.value as value, user.name as user, 
category.description as category, expense.created_at
FROM family_finance.expenses expense
INNER JOIN family_finance.user user ON user.id = expense.created_by
INNER JOIN family_finance.category category ON category.id = expense.category_id
WHERE expense.created_by  = 1;

SELECT 
* 
FROM family_finance.expenses expense 
INNER JOIN family_finance.category category 
 ON category.id = expense.category_id;


SELECT expense.id, expense.description as description, expense.value as value, user.name as user, category.description as category, expense.created_at FROM family_finance.expenses expense INNER JOIN family_finance.user user ON user.id = expense.created_by INNER JOIN family_finance.category category ON category.id = expense.category_id WHERE expense.created_by = 1;

DROP SCHEMA family_finance; 
