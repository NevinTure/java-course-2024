--liquibase formatted sql

--changeset nevinture:1
create table if not exists tg_chat_stackoverflow_link (
    tg_chat_id int references tg_chat(id),
    stackoverflow_link_id int references stackoverflow_link(id),
    primary key (tg_chat_id, stackoverflow_link_id)
)
