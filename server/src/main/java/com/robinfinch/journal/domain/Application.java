package com.robinfinch.journal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * A job application.
 *
 * @recruiter Mark Hoogenboom
 */
@Entity
public class Application extends SyncableObject {

    @Transient
    private Long recruiterId;

    @ManyToOne
    private Recruiter recruiter;

    @Transient
    private Long clientId;

    @ManyToOne
    private Organisation client;    
    
    private String start;

    private String rate;

    private Long stateId;
    
    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }

    @JsonIgnore
    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @JsonIgnore
    public Organisation getClient() {
        return client;
    }

    public void setClient(Organisation client) {
        this.client = client;
    }    
    
    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }    
    
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }    
    
    @Override
    public void prepareBeforeSend() {
        super.prepareBeforeSend();

        if (recruiter == null) {
            recruiterId = 0L;
        } else {
            recruiterId = recruiter.getId();
        }

        if (client == null) {
            clientId = 0L;
        } else {
            clientId = client.getId();
        }        
    }

    @Override
    public void prepareAfterReceive(EntityManager em, JournalOwner owner) {
        super.prepareAfterReceive(em, owner);

        if (recruiterId == 0L) {
            recruiter = null;
        } else {
            recruiter = em.find(Recruiter.class, recruiterId);
        }

        if (clientId == 0L) {
            client = null;
        } else {
            client = em.find(Organisation.class, clientId);
        }        
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Start[id=" + getId()
                + ";owner=" + getOwner()
                + ";recruiterId=" + getRecruiterId()
                + ";recruiter=" + getRecruiter()
                + ";clientId=" + getClientId()
                + ";client=" + getClient()
                + ";start=" + getStart()
                + ";rate=" + getRate()
                + ";stateId=" + getStateId()
                + "]";
    }
}
