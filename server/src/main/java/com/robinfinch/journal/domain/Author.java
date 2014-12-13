package com.robinfinch.journal.domain;

import javax.persistence.Entity;

import static com.robinfinch.journal.server.util.Utils.compare;

/**
 * Author of a {@link com.robinfinch.journal.domain.Title}.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class Author extends SyncableObject implements Comparable<Author> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String getLastName() {
        if (name == null) {
            return null;
        }
        int i = name.lastIndexOf(' ');
        return (i > 0) ? name.substring(i + 1) : name;
    }

    @Override
    public int compareTo(Author that) {
        int c = compare(this.getLastName(), that.getLastName());
        if (c == 0) {
            c = (int) (this.getId() - that.getId());
        }
        return c;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Author[id=" + getId()
                + ";owner=" + getOwner()
                + ";name=" + getName()
                + "]";
    }
}
