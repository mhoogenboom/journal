package com.robinfinch.journal.app.rest;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

import static com.robinfinch.journal.app.util.Constants.HEADER_AUTH_TOKEN;
import static com.robinfinch.journal.app.util.Constants.HEADER_AUTH_TOKEN_TYPE;
import static com.robinfinch.journal.app.util.Constants.HEADER_EMAIL;
import static com.robinfinch.journal.app.util.Constants.HEADER_GCM_REGISTRATION_ID;
import static com.robinfinch.journal.app.util.Constants.HEADER_PASSWORD;

/**
 * The server API.
 *
 * @author Mark Hoogenboom
 */
public interface JournalApi {

    @GET("/user/token")
    public String signin(
            @Header(HEADER_EMAIL) String email,
            @Header(HEADER_PASSWORD) String password,
            @Header(HEADER_AUTH_TOKEN_TYPE) String authTokenType,
            @Header(HEADER_GCM_REGISTRATION_ID) String gcmRegistrationId);

    @Headers({
            "Content-Type: application/json"
    })
    @PUT("/{entity}")
    public long create(
            @Header(HEADER_AUTH_TOKEN) String token,
            @Path("entity") String entity,
            @Body SyncableObjectWrapper object);

    @Headers({
            "Content-Type: application/json"
    })
    @POST("/{entity}/{id}")
    public Response update(
            @Header(HEADER_AUTH_TOKEN) String token,
            @Path("entity") String entity,
            @Path("id") long remoteId,
            @Body SyncableObjectWrapper object);

    @DELETE("/{entity}/{id}")
    public Response delete(
            @Header(HEADER_AUTH_TOKEN) String token,
            @Path("entity") String entity,
            @Path("id") long remoteId);

    @Headers({
            "Accept: application/json",
    })
    @GET("/sync/{revision}")
    public DiffResponse diff(
            @Header(HEADER_AUTH_TOKEN) String token,
            @Path("revision") long latestRevision);
}
