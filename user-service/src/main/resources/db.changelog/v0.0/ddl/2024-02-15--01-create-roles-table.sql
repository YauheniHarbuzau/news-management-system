--liquibase formatted sql
--changeset Author:create_roles_table
CREATE TABLE IF NOT EXISTS roles
(
    id        BIGSERIAL PRIMARY KEY NOT NULL,
    role_type VARCHAR(255)          NOT NULL UNIQUE
);