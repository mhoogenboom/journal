package com.robinfinch.journal.server.gcm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.HashSet;

/**
 * A tickle send to the Google Cloud Messaging service.
 *
 * @author Mark Hoogenboom
 */
public class GcmMessage {

    private static final String COLLAPSE_KEY = "com.robinfinch.journal.tickle";

    private final String collapseKey;
    private final Collection<String> registrationIds;

    public GcmMessage() {
        collapseKey = COLLAPSE_KEY;
        registrationIds = new HashSet<>();
    }

    @JsonProperty("collapse_key")
    public String getCollapseKey() {
        return collapseKey;
    }

    public GcmMessage registrationId(String id) {
        registrationIds.add(id);
        return this;
    }

    @JsonProperty("registration_ids")
    public Collection<String> getRegistrationIds() {
        return registrationIds;
    }
}
