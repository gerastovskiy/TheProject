CREATE TABLE notification
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    contact  VARCHAR NOT NULL,
    type     VARCHAR NOT NULL,
    message  VARCHAR NOT NULL,
    created  TIMESTAMP
);