/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.model.jooq.tables;

import edu.java.scrapper.model.jooq.DefaultSchema;
import edu.java.scrapper.model.jooq.Keys;
import edu.java.scrapper.model.jooq.enums.State;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


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
public class TgChat extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>TG_CHAT</code>
     */
    public static final TgChat TG_CHAT = new TgChat();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>TG_CHAT.ID</code>.
     */
    public final TableField<Record, Long> ID = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>TG_CHAT.STATE</code>.
     */
    public final TableField<Record, State> STATE = createField(DSL.name("STATE"), SQLDataType.VARCHAR.defaultValue(DSL.field(DSL.raw("'DEFAULT'"), SQLDataType.VARCHAR)).asEnumDataType(edu.java.scrapper.model.jooq.enums.State.class), this, "");

    private TgChat(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TgChat(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>TG_CHAT</code> table reference
     */
    public TgChat(String alias) {
        this(DSL.name(alias), TG_CHAT);
    }

    /**
     * Create an aliased <code>TG_CHAT</code> table reference
     */
    public TgChat(Name alias) {
        this(alias, TG_CHAT);
    }

    /**
     * Create a <code>TG_CHAT</code> table reference
     */
    public TgChat() {
        this(DSL.name("TG_CHAT"), null);
    }

    public <O extends Record> TgChat(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, TG_CHAT);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.CONSTRAINT_D;
    }

    @Override
    @NotNull
    public TgChat as(String alias) {
        return new TgChat(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public TgChat as(Name alias) {
        return new TgChat(alias, this);
    }

    @Override
    @NotNull
    public TgChat as(Table<?> alias) {
        return new TgChat(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChat rename(String name) {
        return new TgChat(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChat rename(Name name) {
        return new TgChat(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public TgChat rename(Table<?> name) {
        return new TgChat(name.getQualifiedName(), null);
    }
}
