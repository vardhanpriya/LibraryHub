-- Global holidays (branch_id = NULL)
INSERT INTO holiday (branch_id, date, reason, status, created_at) VALUES
(NULL, '2026-01-01', 'New Year''s Day', 'ACTIVE', CURRENT_TIMESTAMP),
(NULL, '2026-12-25', 'Christmas Day', 'ACTIVE', CURRENT_TIMESTAMP),
(NULL, '2026-07-04', 'Independence Day', 'ACTIVE', CURRENT_TIMESTAMP);


-- Branch 1 specific holidays
INSERT INTO holiday (branch_id, date, reason, status, created_at) VALUES
(1, '2026-03-15', 'Branch 1 Foundation Day', 'ACTIVE', CURRENT_TIMESTAMP),
(1, '2026-10-10', 'Branch 1 Anniversary', 'ACTIVE', CURRENT_TIMESTAMP);

-- Branch 2 specific holidays
INSERT INTO holiday (branch_id, date, reason, status, created_at) VALUES
(2, '2026-04-01', 'Branch 2 Foundation Day', 'ACTIVE', CURRENT_TIMESTAMP),
(2, '2026-11-05', 'Branch 2 Staff Day', 'ACTIVE', CURRENT_TIMESTAMP);

-- Branch 3 specific holidays
INSERT INTO holiday (branch_id, date, reason, status, created_at) VALUES
(3, '2026-06-20', 'Branch 3 Annual Event', 'ACTIVE', CURRENT_TIMESTAMP),
(3, '2026-09-15', 'Branch 3 Local Holiday', 'ACTIVE', CURRENT_TIMESTAMP);
