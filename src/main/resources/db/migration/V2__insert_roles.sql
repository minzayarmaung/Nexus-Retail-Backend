-- Insert core roles
INSERT INTO roles (name, description, disabled, created_at, updated_at, status)
SELECT 'SYSTEM_ADMIN', 'Full system access', false, now(), now(), 1
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'SYSTEM_ADMIN');

INSERT INTO roles (name, description, disabled, created_at, updated_at, status)
SELECT 'OWNER', 'Business owner access', false, now(), now(), 1
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'OWNER');
