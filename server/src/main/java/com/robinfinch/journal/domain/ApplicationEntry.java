package com.robinfinch.journal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * Journal entry describing an application.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class ApplicationEntry extends JournalEntry {

    @Transient
    private Long applicationId;

    @ManyToOne
    private Application application;

    private Long actionId;

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @JsonIgnore
    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    @Override
    public void prepareBeforeSend() {
        super.prepareBeforeSend();

        if (application == null) {
            applicationId = 0L;
        } else {
            applicationId = application.getId();
        }
    }

    @Override
    public void prepareAfterReceive(EntityManager em, JournalOwner owner) {
        super.prepareAfterReceive(em, owner);

        if (applicationId == 0L) {
            application = null;
        } else {
            application = em.find(Application.class, applicationId);
        }
    }

    @Override
    public String toPrettyString() {
        return "";
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.ApplicationEntry[id=" + getId()
                + ";owner=" + getOwner()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";applicationId=" + getApplicationId()
                + ";application=" + getApplication()
                + ";actionId=" + getActionId()
                + "]";
    }
}
