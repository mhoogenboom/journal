package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.Course;

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
 * Facade to the {@link com.robinfinch.journal.domain.Course course}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("course")
public class CourseFacade extends SyncableObjectFacade<Course> {

    public CourseFacade() {
        super(Course.class);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createCourse(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            Course entity) {
        return createEntity(token, entity);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editCourse(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id,
            Course entity) {
        return editEntity(token, id, entity);
    }

    @DELETE
    @Path("{id}")
    public Response removeCourse(
            @HeaderParam(HEADER_AUTH_TOKEN) String token,
            @PathParam("id") Long id) {
        return removeEntity(token, findEntity(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Course findCourse(@PathParam("id") Long id) {
        return findEntity(id);
    }
}
