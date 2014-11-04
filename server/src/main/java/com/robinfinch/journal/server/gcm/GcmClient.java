package com.robinfinch.journal.server.gcm;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import org.apache.cxf.jaxrs.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static com.robinfinch.journal.server.util.Utils.LOG_TAG;

/**
 * Google Cloud Messaging client.
 *
 * @author Mark Hoogenboom
 */
public class GcmClient {

    private static final String ENDPOINT = "https://android.googleapis.com/gcm/send";

    @Resource(name="gcm_api_key")
    private String apiKey;

    public void send(GcmMessage message) {

        Logger.getLogger(LOG_TAG).info("Send tickle to " + message.getRegistrationIds());

        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJaxbJsonProvider());

        WebClient.create(ENDPOINT, providers)
                .header(HttpHeaders.AUTHORIZATION, "key=" + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .post(message, String.class); // not interested in response
    }
}
