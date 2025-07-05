CREATE TABLE delivery
(
    id       BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    date     TIMESTAMP
);