package com.robinfinch.journal.domain;

import javax.persistence.Entity;

/**
 * Author of a {@link com.robinfinch.journal.domain.Title}.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class Author extends SyncableObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Author[id=" + getId()
                + ";owner=" + getOwner()
                + ";name=" + getName()
                + "]";
    }
}
