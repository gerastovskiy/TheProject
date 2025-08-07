CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    last_name  VARCHAR NOT NULL,
    username   VARCHAR NOT NULL UNIQUE,
    email      VARCHAR NOT NULL,
    phone      VARCHAR NOT NULL,
    telegram   INTEGER,
    address    VARCHAR NOT NULL,
    role       VARCHAR NOT NULL
);