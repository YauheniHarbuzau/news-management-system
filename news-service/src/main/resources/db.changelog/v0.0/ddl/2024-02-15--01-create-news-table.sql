--liquibase formatted sql
--changeset Author:create_news_table
CREATE TABLE IF NOT EXISTS news
(
    id          BIGSERIAL PRIMARY KEY       NOT NULL,
    uuid        UUID                        NOT NULL UNIQUE,
    title       VARCHAR(255)                NOT NULL,
    text        TEXT                        NOT NULL,
    author      VARCHAR(255)                NOT NULL,
    create_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    update_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);