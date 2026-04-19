-- =========================
-- ROLES
-- =========================
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- =========================
-- USERS
-- =========================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255),
    nome VARCHAR(255) NOT NULL,
    cognome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    codice_fiscale VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT uk_user_cf UNIQUE (codice_fiscale)
);

-- INDEXES
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_cf ON users(codice_fiscale);
CREATE INDEX idx_user_status ON users(status);

-- =========================
-- USER_ROLES (ManyToMany)
-- =========================
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
);

-- =========================
-- AUDIT LOG
-- =========================
CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    action VARCHAR(255),
    entity_name VARCHAR(255),
    entity_id BIGINT,
    endpoint VARCHAR(255),
    http_method VARCHAR(50),
    status VARCHAR(50),
    created_at TIMESTAMP,
    details JSONB
);

-- =========================
-- DATA INIT
-- =========================

-- ROLES
INSERT INTO roles (id, name) VALUES (1, 'OWNER');
INSERT INTO roles (id, name) VALUES (2, 'OPERATOR');
INSERT INTO roles (id, name) VALUES (3, 'MAINTAINER');
INSERT INTO roles (id, name) VALUES (4, 'DEVELOPER');
INSERT INTO roles (id, name) VALUES (5, 'REPORTER');