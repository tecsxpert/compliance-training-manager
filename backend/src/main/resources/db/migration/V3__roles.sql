-- ============================================
-- V3: Create roles, users, and user_roles tables
-- ============================================

CREATE TABLE IF NOT EXISTS roles (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id       BIGSERIAL    PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id),
    role_id BIGINT NOT NULL REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

-- Seed default roles
INSERT INTO roles (name) VALUES ('ADMIN')   ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('MANAGER') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('VIEWER')  ON CONFLICT (name) DO NOTHING;

-- Seed default admin user (password: admin123 — BCrypt encoded)
INSERT INTO users (username, email, password)
VALUES ('admin', 'admin@compliance.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON CONFLICT (username) DO NOTHING;

-- Assign ADMIN role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ADMIN'
ON CONFLICT DO NOTHING;