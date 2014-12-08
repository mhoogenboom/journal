package com.robinfinch.journal.domain;

import javax.persistence.Entity;

/**
 * An organisation.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class Organisation extends SyncableObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Organisation[id=" + getId()
                + ";owner=" + getOwner()
                + ";name=" + getName()
                + "]";
    }
}
