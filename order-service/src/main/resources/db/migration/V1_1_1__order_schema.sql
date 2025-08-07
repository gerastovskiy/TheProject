create extension if not exists "uuid-ossp";

CREATE TABLE orders
(
    id         BIGSERIAL PRIMARY KEY,
    uuid       uuid not null default uuid_generate_v4(),
    username   VARCHAR NOT NULL,
    amount     NUMERIC,
    status     VARCHAR NOT NULL,
    product_id NUMERIC,
    quantity   NUMERIC,
    contact    VARCHAR NOT NULL,
    type       VARCHAR NOT NULL,
    created    TIMESTAMP,
    updated    TIMESTAMP
);