/*
 * This file is generated by jOOQ.
*/
package com.findit.joog.tables;


import com.findit.joog.Indexes;
import com.findit.joog.Keys;
import com.findit.joog.Public;
import com.findit.joog.tables.records.EmailRecord;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Email extends TableImpl<EmailRecord> {

    private static final long serialVersionUID = -773336365;

    /**
     * The reference instance of <code>public.email</code>
     */
    public static final Email EMAIL = new Email();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EmailRecord> getRecordType() {
        return EmailRecord.class;
    }

    /**
     * The column <code>public.email.id</code>.
     */
    public final TableField<EmailRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('email_id_seq'::regclass)", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.email.student_meeting_id</code>.
     */
    public final TableField<EmailRecord, Long> STUDENT_MEETING_ID = createField("student_meeting_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.email.created_at</code>.
     */
    public final TableField<EmailRecord, OffsetDateTime> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE.defaultValue(org.jooq.impl.DSL.field("timezone('BRT'::text, CURRENT_TIMESTAMP)", org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    /**
     * Create a <code>public.email</code> table reference
     */
    public Email() {
        this(DSL.name("email"), null);
    }

    /**
     * Create an aliased <code>public.email</code> table reference
     */
    public Email(String alias) {
        this(DSL.name(alias), EMAIL);
    }

    /**
     * Create an aliased <code>public.email</code> table reference
     */
    public Email(Name alias) {
        this(alias, EMAIL);
    }

    private Email(Name alias, Table<EmailRecord> aliased) {
        this(alias, aliased, null);
    }

    private Email(Name alias, Table<EmailRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.EMAIL_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<EmailRecord, Integer> getIdentity() {
        return Keys.IDENTITY_EMAIL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EmailRecord> getPrimaryKey() {
        return Keys.EMAIL_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EmailRecord>> getKeys() {
        return Arrays.<UniqueKey<EmailRecord>>asList(Keys.EMAIL_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Email as(String alias) {
        return new Email(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Email as(Name alias) {
        return new Email(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Email rename(String name) {
        return new Email(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Email rename(Name name) {
        return new Email(name, null);
    }
}