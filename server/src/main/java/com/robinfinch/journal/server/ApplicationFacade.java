package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.Application;

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
 * Facade to the {@link com.robinfinch.journal.domain.Application application}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("application")
public class ApplicationFacade extends SyncableObjectFacade<Application> {

    public ApplicationFacade() {
        super(Application.class);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createApplication(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            Application entity) {
        return createEntity(token, entity);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editApplication(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id,
            Application entity) {
        return editEntity(token, id, entity);
    }

    @DELETE
    @Path("{id}")
    public Response removeApplication(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id) {
        return removeEntity(token, findEntity(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Application findApplication(@PathParam("id") Long id) {
        return findEntity(id);
    }
}
