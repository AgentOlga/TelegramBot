-- liquibase formatted sql

-- changeset jrembo:1

CREATE TABLE users
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      INT     NOT NULL,
    nick_name    VARCHAR,
    first_name   VARCHAR,
    last_name    VARCHAR,
    phone_number VARCHAR,
    car_number   VARCHAR,
    email        VARCHAR,
    address      VARCHAR,
    user_type    VARCHAR NOT NULL,-- enum UserType
    user_status  VARCHAR NOT NULL -- enum UserStatus
);

-- changeset jrembo:2

CREATE TABLE animals
(
    id        BIGSERIAL PRIMARY KEY,
    nick_name VARCHAR NOT NULL,
    pet_type  VARCHAR NOT NULL,-- enum PetType
    color     VARCHAR NOT NULL,-- enum Color
    sex       VARCHAR NOT NULL -- enum Sex
);

-- changeset jrembo:3
CREATE TABLE adopters
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT,
    animal_id  BIGINT NOT NULL,
    shelter_id BIGINT NOT NULL
);

-- changeset jrembo:4
CREATE TABLE shelters
(
    id                 BIGSERIAL PRIMARY KEY,
    address_shelter    VARCHAR NOT NULL,
    time_work          VARCHAR NOT NULL,
    driving_directions VARCHAR NOT NULL,
    phone_shelter      VARCHAR NOT NULL,
    phone_security     VARCHAR NOT NULL,
    shelter_type       VARCHAR NOT NULL
);
