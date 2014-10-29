package com.robinfinch.journal.domain;

import javax.persistence.Entity;

/**
 * Security role of an owner of a journal.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class JournalOwnerRole extends PersistableObject {

    private String email;

    private String roleName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.JournalOwnerRole[id=" + getId()
                + ";email=" + getEmail()
                + ";roleName=" + getRoleName()
                + "]";
    }
}
