package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.App;
import com.robinfinch.journal.domain.JournalOwner;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Base class of facades.
 *
 * @author Mark Hoogenboom
 */
public abstract class AbstractFacade {

    protected static final String HEADER_EMAIL = "X-Journal-Email";
    protected static final String HEADER_PASSWORD = "X-Journal-Password";
    protected static final String HEADER_AUTH_TOKEN_TYPE = "X-Journal-Auth-Token-Type";
    protected static final String HEADER_AUTH_TOKEN = "X-Journal-Auth-Token";
    protected static final String HEADER_GCM_REGISTRATION_ID = "X-Journal-Gcm-Registration-Id";

    protected static final int PERSISTENT_CONTEXT_VERSION = 13;

    @PersistenceContext(unitName = "JournalServerPU")
    protected EntityManager em;

    protected JournalOwner findOwnerByEmail(String email) {
        List<JournalOwner> owners = em.createQuery("SELECT o FROM JournalOwner o WHERE o.email = ?1", JournalOwner.class)
                .setParameter(1, email)
                .getResultList();
        return (owners.isEmpty() ? null : owners.get(0));
    }

    protected App findAppByToken(String token) {
        List<App> apps = em.createQuery("SELECT a FROM App a WHERE a.token = ?1", App.class)
                .setParameter(1, token)
                .getResultList();
        return (apps.isEmpty() ? null : apps.get(0));
    }
}
