
-- PERMISSIONS

INSERT INTO permission (name, description, status) VALUES
('BORROW_BOOK', 'Borrow book', 'ACTIVE'),
('RESERVE_BOOK', 'Reserve book', 'ACTIVE'),
('VIEW_OWN_LOANS', 'View own loans', 'ACTIVE'),
('VIEW_OWN_RESERVATIONS', 'View own reservations', 'ACTIVE'),
('VIEW_OWN_FINES', 'View own fines', 'ACTIVE'),
('ADD_BOOK', 'Add book', 'ACTIVE'),
('UPDATE_BOOK', 'Update book', 'ACTIVE'),
('DELETE_BOOK', 'Delete book', 'ACTIVE'),
('MANAGE_BOOK_COPIES', 'Manage book copies', 'ACTIVE'),
('VIEW_REPORTS', 'View reports', 'ACTIVE'),
('MANAGE_LOANS', 'Manage loans', 'ACTIVE'),
('MANAGE_FINES', 'Manage fines', 'ACTIVE'),
('MANAGE_RESERVATIONS', 'Manage reservations', 'ACTIVE'),
('MANAGE_USERS', 'Manage users', 'ACTIVE'),
('MANAGE_ROLES', 'Manage roles', 'ACTIVE'),
('MANAGE_PERMISSIONS', 'Manage permissions', 'ACTIVE'),
('MANAGE_BRANCHES', 'Manage branches', 'ACTIVE'),
('MANAGE_POLICIES', 'Manage policies', 'ACTIVE'),
('VIEW_ACTIVITY_LOGS', 'View activity logs', 'ACTIVE')
ON CONFLICT (name) DO NOTHING;


-- ROLES

INSERT INTO role (name, description, status) VALUES
('MEMBER', 'Member role', 'ACTIVE'),
('LIBRARIAN', 'Librarian role', 'ACTIVE'),
('ADMIN', 'Admin role', 'ACTIVE')
ON CONFLICT (name) DO NOTHING;


-- MEMBER PERMISSIONS

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM role r
JOIN permission p ON p.name IN (
    'BORROW_BOOK',
    'RESERVE_BOOK',
    'VIEW_OWN_LOANS',
    'VIEW_OWN_RESERVATIONS',
    'VIEW_OWN_FINES'
)
WHERE r.name = 'MEMBER'
ON CONFLICT DO NOTHING;


-- LIBRARIAN PERMISSIONS

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM role r
JOIN permission p ON p.name IN (
    'BORROW_BOOK',
    'RESERVE_BOOK',
    'ADD_BOOK',
    'UPDATE_BOOK',
    'DELETE_BOOK',
    'MANAGE_BOOK_COPIES',
    'VIEW_REPORTS',
    'MANAGE_LOANS',
    'MANAGE_FINES',
    'MANAGE_RESERVATIONS'
)
WHERE r.name = 'LIBRARIAN'
ON CONFLICT DO NOTHING;


-- ADMIN = ALL PERMISSIONS

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM role r
CROSS JOIN permission p
WHERE r.name = 'ADMIN'
ON CONFLICT DO NOTHING;
