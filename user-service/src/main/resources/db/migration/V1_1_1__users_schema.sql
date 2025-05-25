CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR NOT NULL UNIQUE,
    email    VARCHAR NOT NULL,
    role     VARCHAR NOT NULL
);