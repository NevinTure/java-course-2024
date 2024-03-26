--liquibase formatted sql

--changeset nevinture:1
create table if not exists tg_chat_link (
    tg_chat_id int references tg_chat(id),
    link_id int references link(id),
    primary key (tg_chat_id, link_id)
)
