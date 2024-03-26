/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.model.jooq.tables;


import edu.java.scrapper.model.jooq.DefaultSchema;
import edu.java.scrapper.model.jooq.Keys;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
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
public class Link extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>LINK</code>
     */
    public static final Link LINK = new Link();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>LINK.ID</code>.
     */
    public final TableField<Record, Long> ID = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>LINK.URL</code>.
     */
    public final TableField<Record, String> URL = createField(DSL.name("URL"), SQLDataType.VARCHAR(1000000000).nullable(false), this, "");

    private Link(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private Link(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>LINK</code> table reference
     */
    public Link(String alias) {
        this(DSL.name(alias), LINK);
    }

    /**
     * Create an aliased <code>LINK</code> table reference
     */
    public Link(Name alias) {
        this(alias, LINK);
    }

    /**
     * Create a <code>LINK</code> table reference
     */
    public Link() {
        this(DSL.name("LINK"), null);
    }

    public <O extends Record> Link(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, LINK);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public Identity<Record, Long> getIdentity() {
        return (Identity<Record, Long>) super.getIdentity();
    }

    @Override
    @NotNull
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.CONSTRAINT_2;
    }

    @Override
    @NotNull
    public List<UniqueKey<Record>> getUniqueKeys() {
        return Arrays.asList(Keys.CONSTRAINT_23);
    }

    @Override
    @NotNull
    public Link as(String alias) {
        return new Link(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public Link as(Name alias) {
        return new Link(alias, this);
    }

    @Override
    @NotNull
    public Link as(Table<?> alias) {
        return new Link(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Link rename(String name) {
        return new Link(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Link rename(Name name) {
        return new Link(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Link rename(Table<?> name) {
        return new Link(name.getQualifiedName(), null);
    }
}
