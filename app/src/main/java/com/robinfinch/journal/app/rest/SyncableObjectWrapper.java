package com.robinfinch.journal.app.rest;

import com.robinfinch.journal.domain.SyncableObject;

/**
 * Wraps a {@link com.robinfinch.journal.domain.SyncableObject} for use in a polymorphic list.
 *
 * @author Mark Hoogenboom
 */
public class SyncableObjectWrapper {

    public final SyncableObject entity;

    public SyncableObjectWrapper(SyncableObject entity) {
        this.entity = entity;
    }
}
