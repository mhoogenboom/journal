package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.ApplicationEntry;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Facade to the {@link com.robinfinch.journal.domain.ApplicationEntry application entry}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("applicationentry")
public class ApplicationEntryFacade extends SyncableObjectFacade<ApplicationEntry> {

    public ApplicationEntryFacade() {
        super(ApplicationEntry.class);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createApplicationEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            ApplicationEntry entity) {
        return createEntity(token, entity);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editApplicationEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id,
            ApplicationEntry entity) {
        return editEntity(token, id, entity);
    }

    @DELETE
    @Path("{id}")
    public Response removeApplicationEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id) {
        return removeEntity(token, findEntity(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public ApplicationEntry findApplicationEntry(@PathParam("id") Long id) {
        return findEntity(id);
    }
}
