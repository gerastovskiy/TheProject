CREATE TABLE account
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR NOT NULL UNIQUE,
    amount   NUMERIC
);