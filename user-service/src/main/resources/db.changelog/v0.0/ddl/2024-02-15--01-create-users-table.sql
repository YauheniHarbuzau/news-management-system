--liquibase formatted sql
--changeset Author:create_users_table
CREATE TABLE IF NOT EXISTS users
(
    id          BIGSERIAL PRIMARY KEY       NOT NULL,
    uuid        UUID                        NOT NULL UNIQUE,
    username    VARCHAR(255)                NOT NULL UNIQUE,
    password    VARCHAR(255)                NOT NULL,
    email       VARCHAR(255)                NOT NULL UNIQUE,
    role_id     BIGINT REFERENCES roles(id),
    create_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    update_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);