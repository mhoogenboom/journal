package com.robinfinch.journal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * A recruiter.
 *
 * @recruiter Mark Hoogenboom
 */
@Entity
public class Recruiter extends SyncableObject {

    private String name;

    @Transient
    private Long organisationId;

    @ManyToOne
    private Organisation organisation;    

    private String phonenumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    @JsonIgnore
    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
    
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public void prepareBeforeSend() {
        super.prepareBeforeSend();

        if (organisation == null) {
            organisationId = 0L;
        } else {
            organisationId = organisation.getId();
        }        
    }

    @Override
    public void prepareAfterReceive(EntityManager em, JournalOwner owner) {
        super.prepareAfterReceive(em, owner);

        if (organisationId == 0L) {
            organisation = null;
        } else {
            organisation = em.find(Organisation.class, organisationId);
        }        
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Name[id=" + getId()
                + ";owner=" + getOwner()
                + ";name=" + getName()
                + ";organisationId=" + getOrganisationId()
                + ";organisation=" + getOrganisation()
                + ";phonenumber=" + getPhonenumber()
                + "]";
    }
}
