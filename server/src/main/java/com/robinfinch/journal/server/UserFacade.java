package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.App;
import com.robinfinch.journal.domain.JournalOwner;
import com.robinfinch.journal.domain.JournalOwnerRole;
import com.robinfinch.journal.domain.SyncLog;
import com.robinfinch.journal.server.rest.DiffResponse;
import com.robinfinch.journal.server.util.Utils;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.robinfinch.journal.server.util.Utils.LOG_TAG;

/**
 * Facade to the {@link com.robinfinch.journal.domain.JournalOwner journal owner}
 * resource.
 *
 * @author Mark Hoogenboom
 */
@Stateless
@Path("user")
public class UserFacade extends AbstractFacade {

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response register(JournalOwner owner) {

        JournalOwner existingOwner = findOwnerByEmail(owner.getEmail());
        if (existingOwner != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        Logger.getLogger(LOG_TAG).info("Register " + owner);

        em.persist(owner);

        JournalOwnerRole role = new JournalOwnerRole();
        role.setEmail(owner.getEmail());
        role.setRoleName("journalowner");

        em.persist(role);

        return Response.ok().build();
    }

    @GET
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    public Response signin(
            @HeaderParam(HEADER_EMAIL) String email,
            @HeaderParam(HEADER_PASSWORD) String passwordHash,
            @HeaderParam(HEADER_GCM_REGISTRATION_ID) String gcmRegistrationId) {

        JournalOwner owner = findOwnerByEmail(email);
        if ((owner == null) || !owner.getPasswordHash().equals(passwordHash)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Logger.getLogger(LOG_TAG).info("Sign in " + email);

        App app = new App();
        app.setOwner(owner);
        app.setToken(generateToken(owner));
        app.setGcmRegistrationId(gcmRegistrationId);
        em.persist(app);

        return Response.ok(app.getToken()).build();
    }

    private String generateToken(JournalOwner owner) {
        String token = new StringBuilder()
                .append("Jour")
                .append(owner.getId())
                .append(owner.getName())
                .append(System.currentTimeMillis())
                .append("nal")
                .toString();
        return Utils.hash(token, "MD5");
    }
}
