package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.Organisation;

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
 * Facade to the {@link com.robinfinch.journal.domain.Organisation organisation}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("organisation")
public class OrganisationFacade extends SyncableObjectFacade<Organisation> {

    public OrganisationFacade() {
        super(Organisation.class);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createOrganisation(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            Organisation entity) {
        return createEntity(token, entity);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editOrganisation(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id,
            Organisation entity) {
        return editEntity(token, id, entity);
    }

    @DELETE
    @Path("{id}")
    public Response removeOrganisation(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id) {
        return removeEntity(token, findEntity(id));
    }

    @GET
    @Path("{id}")
    @Produces({"organisation/json"})
    public Organisation findOrganisation(@PathParam("id") Long id) {
        return findEntity(id);
    }
}
