--liquibase formatted sql

--changeset nevinture:1
create table if not exists tg_chat_git_link (
    tg_chat_id int references tg_chat(id),
    git_link_id int references git_link(id),
    primary key (tg_chat_id, git_link_id)
)
