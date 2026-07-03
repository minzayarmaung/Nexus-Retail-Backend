-- Flyway migration: create password_validation_policy table and unique index on key
CREATE TABLE IF NOT EXISTS password_validation_policy (
  id BIGSERIAL PRIMARY KEY,
  regex VARCHAR(255),
  description VARCHAR(255),
  active BOOLEAN NOT NULL,
  key VARCHAR(255)
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_password_validation_policy_key ON password_validation_policy (key);
