--liquibase formatted sql
--changeset Author:create_comments_table
CREATE TABLE IF NOT EXISTS comments
(
    id          BIGSERIAL PRIMARY KEY       NOT NULL,
    uuid        UUID                        NOT NULL UNIQUE,
    text        TEXT                        NOT NULL,
    username    VARCHAR(255)                NOT NULL,
    create_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    update_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    news_id     BIGINT REFERENCES news(id)
);