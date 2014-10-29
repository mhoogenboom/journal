package com.robinfinch.journal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Base class of all classes that are synced to the server.
 *
 * @author Mark Hoogenboom
 */
@Entity
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public abstract class SyncableObject extends PersistableObject {

    @ManyToOne
    private JournalOwner owner;

    @JsonIgnore
    public JournalOwner getOwner() {
        return owner;
    }

    public void setOwner(JournalOwner owner) {
        this.owner = owner;
    }
}
