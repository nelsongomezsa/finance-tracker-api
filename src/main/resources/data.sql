INSERT INTO categories (name, type) VALUES ('Nomina', 'INCOME');
INSERT INTO categories (name, type) VALUES ('Freelance', 'INCOME');
INSERT INTO categories (name, type) VALUES ('Alquiler', 'EXPENSE');
INSERT INTO categories (name, type) VALUES ('Comida', 'EXPENSE');
INSERT INTO categories (name, type) VALUES ('Transporte', 'EXPENSE');

INSERT INTO transactions (description, amount, date, category_id) VALUES ('Nomina de agosto', 1800.00, '2026-08-01', 1);
INSERT INTO transactions (description, amount, date, category_id) VALUES ('Proyecto freelance', 250.00, '2026-08-03', 2);
INSERT INTO transactions (description, amount, date, category_id) VALUES ('Alquiler piso', 650.00, '2026-08-02', 3);
INSERT INTO transactions (description, amount, date, category_id) VALUES ('Compra semanal', 85.40, '2026-08-05', 4);
INSERT INTO transactions (description, amount, date, category_id) VALUES ('Abono transporte', 54.60, '2026-08-01', 5);
