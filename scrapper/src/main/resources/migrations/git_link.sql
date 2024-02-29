--liquibase formatted sql

--changeset nevinture:1
create table if not exists git_link (
    id int primary key generated by default as identity,
    tg_chat_id int references tg_chat(id),
    last_check_at timestamp,
    last_update_at timestamp,
    last_push_at timestamp
);
