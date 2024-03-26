--liquibase formatted sql

--changeset nevinture:1
create table if not exists link (
    id int primary key generated always as identity,
    url text unique not null
)
