package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.StudyEntry;

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
 * Facade to the {@link com.robinfinch.journal.domain.StudyEntry study entry}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("studyentry")
public class StudyEntryFacade extends SyncableObjectFacade<StudyEntry> {

    public StudyEntryFacade() {
        super(StudyEntry.class);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createStudyEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            StudyEntry entity) {
        return createEntity(token, entity);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editStudyEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id,
            StudyEntry entity) {
        return editEntity(token, id, entity);
    }

    @DELETE
    @Path("{id}")
    public Response removeStudyEntry(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id) {
        return removeEntity(token, findEntity(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public StudyEntry findStudyEntry(@PathParam("id") Long id) {
        return findEntity(id);
    }
}
