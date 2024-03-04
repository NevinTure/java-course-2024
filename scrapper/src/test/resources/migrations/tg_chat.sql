--liquibase formatted sql

--changeset nevinture:1
create type state as enum ('DEFAULT','WAITING_TRACK','WAITING_UNTRACK');
create table if not exists tg_chat (
    id int primary key,
    state state default 'DEFAULT'
);
