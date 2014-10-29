package com.robinfinch.journal.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * An installation of an app.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class App extends PersistableObject {

    @ManyToOne
    private JournalOwner owner;

    private String token;

    private String gcmRegistrationId;

    public JournalOwner getOwner() {
        return owner;
    }

    public void setOwner(JournalOwner owner) {
        this.owner = owner;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGcmRegistrationId() {
        return gcmRegistrationId;
    }

    public void setGcmRegistrationId(String gcmRegistrationId) {
        this.gcmRegistrationId = gcmRegistrationId;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.JournalApp[id=" + getId()
                + ";owner=" + getOwner()
                + ";gcmRegistrationId=" + getGcmRegistrationId()
                + " ]";
    }
}
