package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.ReadEntry;

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
 * Facade to the {@link com.robinfinch.journal.domain.ReadEntry ReadEntry}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("readentry")
public class ReadEntryFacade extends SyncableObjectFacade<ReadEntry> {

    public ReadEntryFacade() {
        super(ReadEntry.class);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createReadEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            ReadEntry entity) {
        return createEntity(token, entity);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editReadEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id,
            ReadEntry entity) {
        return editEntity(token, id, entity);
    }

    @DELETE
    @Path("{id}")
    public Response removeReadEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id) {
        return removeEntity(token, findEntity(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public ReadEntry findReadEntry(@PathParam("id") Long id) {
        return findEntity(id);
    }
}
