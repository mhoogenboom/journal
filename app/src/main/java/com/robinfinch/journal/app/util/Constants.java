package com.robinfinch.journal.app.util;

/**
 * Constants.
 *
 * @author Mark Hoogenboom
 */
public interface Constants {

    String LOG_TAG = "com.robinfinch.journal.log";

    String ARG_AUTH_TOKEN_TYPE = "auth_token_type";
    String ARG_PASSWORD = "password";
    String ARG_URI = "com.robinfinch.journal.arg_uri";
    String ARG_SELECTED_ID = "com.robinfinch.journal.arg_selected_id";

    String HEADER_EMAIL = "X-Journal-Email";
    String HEADER_PASSWORD = "X-Journal-Password";
    String HEADER_AUTH_TOKEN_TYPE = "X-Journal-Auth-Token-Type";
    String HEADER_AUTH_TOKEN = "X-Journal-Auth-Token";
    String HEADER_GCM_REGISTRATION_ID = "X-Journal-Gcm-Registration-Id";
}
