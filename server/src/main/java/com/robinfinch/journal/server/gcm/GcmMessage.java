package com.robinfinch.journal.server.gcm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * A tickle send to the Google Cloud Messaging service.
 *
 * @author Mark Hoogenboom
 */
public class GcmMessage {

    private static final String COLLAPSE_KEY = "com.robinfinch.journal.tickle";

    private final String collapseKey;
    private final List<String> registrationIds;

    public GcmMessage() {
        collapseKey = COLLAPSE_KEY;
        registrationIds = new ArrayList<>();
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
    public List<String> getRegistrationIds() {
        return registrationIds;
    }
}
