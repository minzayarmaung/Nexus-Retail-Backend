-- Create admin user and assign SYSTEM_ADMIN role
-- Password is BCrypt-hashed for 'password'
INSERT INTO users (username, email, password, first_name, last_name, generated_password, created_at, updated_at, status)
SELECT 'nexus', 'nexusretail@gmail.com', '$2b$12$mtrzWyeKEGtSl7MQLRN1B.7.jnQcNcPSpCEAvOfE0qLjRh9rkeMuy', 'Nexus', 'Admin', false, now(), now(), 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'nexus');

-- Assign role
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'nexus' AND r.name = 'SYSTEM_ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM user_role ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
