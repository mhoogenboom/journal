package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.App;
import com.robinfinch.journal.domain.SyncLog;
import com.robinfinch.journal.server.rest.DiffResponse;

import java.util.List;
import java.util.logging.Logger;

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

        DiffResponse response = new DiffResponse(latestRevision);

        List<SyncLog> logs = findLogs(app.getOwner().getId(), app.getId(), latestRevision);
        for (SyncLog log : logs) {
            response.include(log);
        }

        return Response.ok(response).build();
    }

    private List<SyncLog> findLogs(Long ownerId, Long appId, Long latestRevision) {
        return em.createQuery("SELECT l FROM SyncLog l" +
                " WHERE l.id > ?1 AND l.modifier.owner.id = ?2 AND NOT l.modifier.id = ?3 ORDER BY l.id ASC")
                .setParameter(1, latestRevision)
                .setParameter(2, ownerId)
                .setParameter(3, appId)
                .getResultList();
    }
}
