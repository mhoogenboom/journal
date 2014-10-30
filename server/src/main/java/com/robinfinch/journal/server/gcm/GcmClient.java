package com.robinfinch.journal.server.gcm;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Google Cloud Messaging client.
 *
 * @author Mark Hoogenboom
 */
public class GcmClient {

    private static final String ENDPOINT = "https://android.googleapis.com/gcm/send";

    private static final String HEADER_AUTHENTICATION = "Authentication";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

//    private Client client;

    @PostConstruct
    public void init() {
//        if (client == null) {
//            client = ClientBuilder.newClient();
//        }
    }

    public void send(GcmMessage message) {

//        client.target(ENDPOINT)
//                    .request()
//                    .header(HEADER_AUTHENTICATION, "key=" + key)
//                    .header(HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON)
//                    .post(Entity.json(message));
    }

    @PreDestroy
    public void close() {
//        if (client != null) {
//            client.close();
//            client = null;
//        }
    }
}
