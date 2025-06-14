CREATE TABLE orders
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    amount   NUMERIC,
    status   VARCHAR NOT NULL
);