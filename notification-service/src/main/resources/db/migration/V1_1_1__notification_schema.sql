CREATE TABLE notification
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    type     VARCHAR NOT NULL,
    message  VARCHAR
);