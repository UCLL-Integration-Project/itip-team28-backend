DROP TABLE IF EXISTS "users";

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    username TEXT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL
);