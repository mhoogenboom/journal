package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.TravelEntry;

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
 * Facade to the {@link com.robinfinch.journal.domain.TravelEntry travel entry}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("travelentry")
public class TravelEntryFacade extends SyncableObjectFacade<TravelEntry> {

    public TravelEntryFacade() {
        super(TravelEntry.class);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createTravelEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            TravelEntry entity) {
        return createEntity(token, entity);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editTravelEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id,
            TravelEntry entity) {
        return editEntity(token, id, entity);
    }

    @DELETE
    @Path("{id}")
    public Response removeTravelEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id) {
        return removeEntity(token, findEntity(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public TravelEntry findTravelEntry(@PathParam("id") Long id) {
        return findEntity(id);
    }
}
