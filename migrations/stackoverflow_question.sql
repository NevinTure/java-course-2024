--liquibase formatted sql

--changeset nevinture:1
create table if not exists stackoverflow_question (
    id int primary key generated always as identity,
    link_id int references link(id),
    urn text unique not null,
    last_check_at timestamp with time zone not null,
    last_update_at timestamp  with time zone not null,
    answers int default 0
)
