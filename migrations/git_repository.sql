--liquibase formatted sql

--changeset nevinture:1
create table if not exists git_repository (
    id bigint generated always as identity primary key,
    link_id bigint references link(id),
    urn text unique not null,
    last_check_at timestamp with time zone not null,
    last_update_at timestamp with time zone not null,
    last_push_at timestamp with time zone not null
)
