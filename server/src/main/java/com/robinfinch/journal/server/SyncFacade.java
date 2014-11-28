package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.App;
import com.robinfinch.journal.domain.SyncLog;
import com.robinfinch.journal.server.rest.DiffResponse;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static com.robinfinch.journal.server.util.Utils.LOG_TAG;

/**
 * Facade to the downward sync.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("sync")
public class SyncFacade extends AbstractFacade {

    @Resource(name="version_code")
    private Integer versionCode;


    @GET
    @Path("/{latestRevision}")
    public Response diff(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("latestRevision") Long latestRevision) {

        final App app = findAppByToken(token);
        if (app == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Logger.getLogger(LOG_TAG).info("Diff for " + app);

        Revision revision = new Revision();
        revision.setCodeVersion(versionCode);
        revision.setDataDefinitionVersion(PERSISTENT_CONTEXT_VERSION);
        revision.setDataVersion(latestRevision);

        DiffResponse response = new DiffResponse(revision);

        List<SyncLog> logs;
        if (latestRevision == 0) {
            Logger.getLogger(LOG_TAG).info("Sync everything, including changes by the app itself");
            logs = findAllLogs(app);
        } else {
            logs = findLogs(app, latestRevision);
        }
        for (SyncLog log : logs) {
            response.include(log);
        }

        return Response.ok(response).build();
    }

    private List<SyncLog> findAllLogs(App modifier) {
        return em.createQuery("SELECT l FROM SyncLog l" +
                " WHERE l.modifier.owner.id = ?1" +
                " ORDER BY l.id ASC", SyncLog.class)
                .setParameter(1, modifier.getOwner().getId())
                .getResultList();
    }

    private List<SyncLog> findLogs(App modifier, Long latestRevision) {
        return em.createQuery("SELECT l FROM SyncLog l" +
                " WHERE l.id > ?1 AND l.modifier.owner.id = ?2 AND NOT l.modifier.id = ?3" +
                " ORDER BY l.id ASC", SyncLog.class)
                .setParameter(1, latestRevision)
                .setParameter(2, modifier.getOwner().getId())
                .setParameter(3, modifier.getId())
                .getResultList();
    }
}
