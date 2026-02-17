-- liquibase formatted sql

-- changeset antony:002-create-admin-user

INSERT INTO users (
    id,
    name,
    email,
    password_hash,
    phone_number,
    user_type,
    role,
    is_active,
    created_at
)
SELECT
    'ac2547ce-f91e-4ad5-9512-03388f1faea2',
    'Admin Admin',
    'admin@mail.com',
    '$2a$10$e7JHkU/EnQm35yvFYQlmBerO67G9.l9Qf7G/oJaxB8Hsa6swa.cRm',
    '+4916713124168',
    'EDUCATOR',
    'ADMIN',
    true,
    NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@mail.com'
);