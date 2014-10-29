package com.robinfinch.journal.domain;

import android.content.ContentValues;

import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Base class for all classes that are stored in the db.
 * 
 * author Mark Hoogenboom
 */
public abstract class PersistableObject {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract ContentValues toValues();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id == null ? 0 : id.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        PersistableObject that = (PersistableObject) object;
        return !differs(this.id, that.id);
    }
}
