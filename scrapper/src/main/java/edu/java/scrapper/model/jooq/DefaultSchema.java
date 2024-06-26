/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.model.jooq;

import edu.java.scrapper.model.jooq.tables.GitRepository;
import edu.java.scrapper.model.jooq.tables.Link;
import edu.java.scrapper.model.jooq.tables.StackoverflowQuestion;
import edu.java.scrapper.model.jooq.tables.TgChat;
import edu.java.scrapper.model.jooq.tables.TgChatLink;
import java.util.Arrays;
import java.util.List;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>GIT_REPOSITORY</code>.
     */
    public final GitRepository GIT_REPOSITORY = GitRepository.GIT_REPOSITORY;

    /**
     * The table <code>LINK</code>.
     */
    public final Link LINK = Link.LINK;

    /**
     * The table <code>STACKOVERFLOW_QUESTION</code>.
     */
    public final StackoverflowQuestion STACKOVERFLOW_QUESTION = StackoverflowQuestion.STACKOVERFLOW_QUESTION;

    /**
     * The table <code>TG_CHAT</code>.
     */
    public final TgChat TG_CHAT = TgChat.TG_CHAT;

    /**
     * The table <code>TG_CHAT_LINK</code>.
     */
    public final TgChatLink TG_CHAT_LINK = TgChatLink.TG_CHAT_LINK;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    @NotNull
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    @NotNull
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            GitRepository.GIT_REPOSITORY,
            Link.LINK,
            StackoverflowQuestion.STACKOVERFLOW_QUESTION,
            TgChat.TG_CHAT,
            TgChatLink.TG_CHAT_LINK
        );
    }
}
