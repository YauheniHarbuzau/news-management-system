--liquibase formatted sql
--changeset Author:insert_into_users_table
INSERT INTO users (uuid, username, password, email, role_id, create_date, update_date)
VALUES ('72d8aeda-5f2c-4080-bc36-e0222f97f078', 'Admin', '$2a$10$tRqCwey5VGn8qnLCGjM4m.nhFfyL.qu5O0ov.Zh82vDYAi2drM41K', 'admin@gmail.com', 1, '2024-02-15 09:00:00', '2024-02-15 09:00:00'),
       ('30e208f0-3092-4de1-b5d8-81a1449c3eef', 'Journalist', '$2a$10$FuTqEmV.J0ElIVIV71XJJ.PHUKFuEtXMoC0hxW2XldxFVqQuD0w52', 'journalist@gmail.com', 2, '2024-02-15 09:00:00', '2024-02-15 09:00:00'),
       ('9c7987d6-1e76-4288-b486-96519f60a400', 'Subscriber', '$2a$10$FXpX59UYhE04edQg.F1.lOoUVGjaw5LEAqP../gb/AKP39l0hwgT.', 'subscriber@gmail.com', 3, '2024-02-15 09:00:00', '2024-02-15 09:00:00');