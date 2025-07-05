CREATE TABLE product
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR NOT NULL UNIQUE,
    quantity INTEGER
);