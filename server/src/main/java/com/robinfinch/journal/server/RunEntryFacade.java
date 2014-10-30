package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.RunEntry;

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
 * Facade to the {@link com.robinfinch.journal.domain.RunEntry run entry}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("runentry")
public class RunEntryFacade extends SyncableObjectFacade<RunEntry> {

    public RunEntryFacade() {
        super(RunEntry.class);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            RunEntry entity) {
        return createEntity(token, entity);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response edit(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id,
            RunEntry entity) {
        return editEntity(token, id, entity);
    }

    @DELETE
    @Path("{id}")
    public Response remove(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id) {
        return removeEntity(token, findEntity(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public RunEntry find(@PathParam("id") Long id) {
        return findEntity(id);
    }
}
