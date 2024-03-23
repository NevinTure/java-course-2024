--liquibase formatted sql

--changeset nevinture:1
create table if not exists link (
    id bigint primary key generated always as identity,
    url text unique not null
)
