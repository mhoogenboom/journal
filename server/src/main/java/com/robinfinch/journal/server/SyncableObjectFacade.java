package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.App;
import com.robinfinch.journal.domain.SyncLog;
import com.robinfinch.journal.domain.SyncableObject;
import com.robinfinch.journal.server.gcm.GcmClient;
import com.robinfinch.journal.server.gcm.GcmMessage;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static com.robinfinch.journal.server.util.Utils.LOG_TAG;

/**
 * Base class of syncable object facades.
 *
 * @param <T>
 * @author Mark Hoogenboom
 */
public abstract class SyncableObjectFacade<T extends SyncableObject> extends AbstractFacade {

    @Inject
    private GcmClient gcmClient;

    private final Class<T> entityClass;

    public SyncableObjectFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Response createEntity(String token, T entity) {
        App app = findAppByToken(token);
        if (app == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Logger.getLogger(LOG_TAG).info("Create " + entity);

        entity.setId(null); // overwrite the client's id
        entity.prepareAfterReceive(em, app.getOwner());
        em.persist(entity);
        em.flush();

        log(app, entity, entity.getId());

        return Response.ok(entity.getId()).build();
    }

    protected Response editEntity(String token, Long id, T entity) {
        App app = findAppByToken(token);
        if (app == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (em.find(entity.getClass(), id) == null) {
            // already deleted
        } else {
            Logger.getLogger(LOG_TAG).info("Edit " + entity);

            entity.setId(id); // overwrite the client's id
            entity.prepareAfterReceive(em, app.getOwner());
            em.merge(entity);

            log(app, entity, entity.getId());
        }

        return Response.ok().build();
    }

    protected Response removeEntity(String token, T entity) {
        App app = findAppByToken(token);
        if (app == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (entity == null) {
            // already deleted
        } else {
            Logger.getLogger(LOG_TAG).info("Remove " + entity);

            log(app, null, entity.getId());

            em.remove(entity);
        }

        return Response.ok().build();
    }

    protected T findEntity(Long id) {
        return em.find(entityClass, id);
    }

    private void log(final App modifier, T entity, Long entityId) {
        SyncLog log = new SyncLog();
        log.setModifier(modifier);
        log.setChangedEntity(entity);
        log.setDeletedEntityClass(entityClass.getName());
        log.setDeletedEntityId(entityId);
        em.persist(log);

        tickle(modifier);
    }

    private void tickle(App modifier) {
        List<App> apps = em.createQuery("SELECT a FROM App a" +
                " WHERE a.owner.id = ?1 AND NOT a.id = ?2", App.class)
                .setParameter(1, modifier.getOwner().getId())
                .setParameter(2, modifier.getId())
                .getResultList();

        if (!apps.isEmpty()) {
            GcmMessage message = new GcmMessage();
            for (App app : apps) {
                if (app.getGcmRegistrationId() == null) {
                    Logger.getLogger(LOG_TAG).info("Can't tickle " + app);
                } else {
                    message.registrationId(app.getGcmRegistrationId());
                }
            }
            gcmClient.send(message);
        }
    }
}
