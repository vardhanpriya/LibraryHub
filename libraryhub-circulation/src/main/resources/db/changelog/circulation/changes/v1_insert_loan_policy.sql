
-- Global Policies (applies to all branches and roles)

INSERT INTO loan_policy (branch_id, role_id, max_books, loan_days, grace_days, fine_per_day, status)
VALUES
(NULL, NULL, 5, 14, 2, 1.00, 'ACTIVE'),
(NULL, NULL, 3, 7, 1, 1.50, 'ACTIVE'),
(NULL, NULL, 6, 21, 3, 0.80, 'ACTIVE'),
(NULL, NULL, 4, 10, 1, 1.20, 'ACTIVE');


-- Branch-specific Policies (all roles)

INSERT INTO loan_policy (branch_id, role_id, max_books, loan_days, grace_days, fine_per_day, status)
VALUES
(1, NULL, 6, 15, 2, 1.20, 'ACTIVE'),
(1, NULL, 4, 10, 1, 1.50, 'ACTIVE'),
(1, NULL, 5, 12, 2, 1.10, 'ACTIVE'),
(2, NULL, 7, 21, 3, 0.90, 'ACTIVE'),
(2, NULL, 5, 14, 2, 1.10, 'ACTIVE'),
(2, NULL, 6, 18, 2, 0.95, 'ACTIVE');


-- Role-specific Policies (all branches)

INSERT INTO loan_policy (branch_id, role_id, max_books, loan_days, grace_days, fine_per_day, status)
VALUES
(NULL, 1, 4, 12, 2, 1.00, 'ACTIVE'),  -- Role 1 = Student
(NULL, 1, 5, 15, 2, 1.20, 'ACTIVE'),
(NULL, 2, 10, 28, 5, 0.50, 'ACTIVE'), -- Role 2 = Faculty
(NULL, 2, 8, 21, 3, 0.80, 'ACTIVE');


--  Branch + Role-specific Policies

INSERT INTO loan_policy (branch_id, role_id, max_books, loan_days, grace_days, fine_per_day, status)
VALUES
(1, 1, 5, 14, 2, 1.00, 'ACTIVE'), -- Student in Branch 1
(1, 2, 10, 28, 5, 0.50, 'ACTIVE'), -- Faculty in Branch 1
(2, 1, 3, 10, 1, 1.50, 'ACTIVE'),  -- Student in Branch 2
(2, 2, 8, 21, 3, 0.80, 'ACTIVE');  -- Faculty in Branch 2