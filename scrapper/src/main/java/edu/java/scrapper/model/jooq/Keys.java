/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.model.jooq;


import edu.java.scrapper.model.jooq.tables.GitRepository;
import edu.java.scrapper.model.jooq.tables.Link;
import edu.java.scrapper.model.jooq.tables.StackoverflowQuestion;
import edu.java.scrapper.model.jooq.tables.TgChat;
import edu.java.scrapper.model.jooq.tables.TgChatLink;

import javax.annotation.processing.Generated;

import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<Record> CONSTRAINT_3 = Internal.createUniqueKey(GitRepository.GIT_REPOSITORY, DSL.name("CONSTRAINT_3"), new TableField[] { GitRepository.GIT_REPOSITORY.ID }, true);
    public static final UniqueKey<Record> CONSTRAINT_392 = Internal.createUniqueKey(GitRepository.GIT_REPOSITORY, DSL.name("CONSTRAINT_392"), new TableField[] { GitRepository.GIT_REPOSITORY.URN }, true);
    public static final UniqueKey<Record> CONSTRAINT_2 = Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_2"), new TableField[] { Link.LINK.ID }, true);
    public static final UniqueKey<Record> CONSTRAINT_23 = Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_23"), new TableField[] { Link.LINK.URL }, true);
    public static final UniqueKey<Record> CONSTRAINT_9 = Internal.createUniqueKey(StackoverflowQuestion.STACKOVERFLOW_QUESTION, DSL.name("CONSTRAINT_9"), new TableField[] { StackoverflowQuestion.STACKOVERFLOW_QUESTION.ID }, true);
    public static final UniqueKey<Record> CONSTRAINT_9D3 = Internal.createUniqueKey(StackoverflowQuestion.STACKOVERFLOW_QUESTION, DSL.name("CONSTRAINT_9D3"), new TableField[] { StackoverflowQuestion.STACKOVERFLOW_QUESTION.URN }, true);
    public static final UniqueKey<Record> CONSTRAINT_D = Internal.createUniqueKey(TgChat.TG_CHAT, DSL.name("CONSTRAINT_D"), new TableField[] { TgChat.TG_CHAT.ID }, true);
    public static final UniqueKey<Record> CONSTRAINT_816 = Internal.createUniqueKey(TgChatLink.TG_CHAT_LINK, DSL.name("CONSTRAINT_816"), new TableField[] { TgChatLink.TG_CHAT_LINK.TG_CHAT_ID, TgChatLink.TG_CHAT_LINK.LINK_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<Record, Record> CONSTRAINT_39 = Internal.createForeignKey(GitRepository.GIT_REPOSITORY, DSL.name("CONSTRAINT_39"), new TableField[] { GitRepository.GIT_REPOSITORY.LINK_ID }, Keys.CONSTRAINT_2, new TableField[] { Link.LINK.ID }, true);
    public static final ForeignKey<Record, Record> CONSTRAINT_9D = Internal.createForeignKey(StackoverflowQuestion.STACKOVERFLOW_QUESTION, DSL.name("CONSTRAINT_9D"), new TableField[] { StackoverflowQuestion.STACKOVERFLOW_QUESTION.LINK_ID }, Keys.CONSTRAINT_2, new TableField[] { Link.LINK.ID }, true);
    public static final ForeignKey<Record, Record> CONSTRAINT_8 = Internal.createForeignKey(TgChatLink.TG_CHAT_LINK, DSL.name("CONSTRAINT_8"), new TableField[] { TgChatLink.TG_CHAT_LINK.TG_CHAT_ID }, Keys.CONSTRAINT_D, new TableField[] { TgChat.TG_CHAT.ID }, true);
    public static final ForeignKey<Record, Record> CONSTRAINT_81 = Internal.createForeignKey(TgChatLink.TG_CHAT_LINK, DSL.name("CONSTRAINT_81"), new TableField[] { TgChatLink.TG_CHAT_LINK.LINK_ID }, Keys.CONSTRAINT_2, new TableField[] { Link.LINK.ID }, true);
}
