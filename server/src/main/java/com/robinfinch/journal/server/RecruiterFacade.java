package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.Recruiter;

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
 * Facade to the {@link com.robinfinch.journal.domain.Recruiter recruiter}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("recruiter")
public class RecruiterFacade extends SyncableObjectFacade<Recruiter> {

    public RecruiterFacade() {
        super(Recruiter.class);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createRecruiter(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            Recruiter entity) {
        return createEntity(token, entity);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editRecruiter(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id,
            Recruiter entity) {
        return editEntity(token, id, entity);
    }

    @DELETE
    @Path("{id}")
    public Response removeRecruiter(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id) {
        return removeEntity(token, findEntity(id));
    }

    @GET
    @Path("{id}")
    @Produces({"recruiter/json"})
    public Recruiter findRecruiter(@PathParam("id") Long id) {
        return findEntity(id);
    }
}
