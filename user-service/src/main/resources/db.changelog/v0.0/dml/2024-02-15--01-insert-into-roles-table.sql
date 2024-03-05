--liquibase formatted sql
--changeset Author:insert_into_roles_table
INSERT INTO roles (id, role_type)
VALUES (1, 'ADMIN'),
       (2, 'JOURNALIST'),
       (3, 'SUBSCRIBER');