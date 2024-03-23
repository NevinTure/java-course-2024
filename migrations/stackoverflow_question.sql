--liquibase formatted sql

--changeset nevinture:1
create table if not exists stackoverflow_question (
    id bigint primary key generated always as identity,
    link_id bigint references link(id),
    urn text unique not null,
    last_check_at timestamp with time zone not null,
    last_update_at timestamp  with time zone not null,
    answers int default 0
)
