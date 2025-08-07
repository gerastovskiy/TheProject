create extension if not exists "uuid-ossp";

CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    uuid        uuid not null default uuid_generate_v4(),
    name        VARCHAR NOT NULL UNIQUE,
    quantity    INTEGER NOT NULL,
    price       DECIMAL NOT NULL,
    active      BOOLEAN NOT NULL,
    description VARCHAR,
    created     TIMESTAMP,
    updated     TIMESTAMP
);