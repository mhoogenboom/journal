package com.robinfinch.journal.domain;

import javax.persistence.Entity;

/**
 * Owner of a journal.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class JournalOwner extends PersistableObject {

    private String name;

    private String email;

    private String passwordHash;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.JournalOwner[id=" + getId()
                + ";name=" + getName()
                + ";email=" + getEmail()
                + "]";
    }
}
