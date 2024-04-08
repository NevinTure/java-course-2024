--liquibase formatted sql

--changeset nevinture:1
create table if not exists link (
    id bigint generated always as identity primary key,
    url text unique not null
)
