--liquibase formatted sql

--changeset author:r.r.amirov runInTransaction:true failOnError:true
CREATE TABLE expenses (
                       id SERIAL PRIMARY KEY,
                       userId bigint NOT NULL,
                       amount bigint NOT NULL,
                       description VARCHAR(255)
);
--rollback drop table users
