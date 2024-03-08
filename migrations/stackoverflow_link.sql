--liquibase formatted sql

--changeset nevinture:1
create table if not exists stackoverflow_link (
    id int primary key generated by default as identity,
    url text not null,
    tg_chat_id int references tg_chat(id),
    last_check_at timestamp with time zone not null,
    last_update_at timestamp  with time zone not null,
    answers int default 0
);
