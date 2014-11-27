package com.robinfinch.journal.app.util;

import android.content.Context;
import android.net.Uri;

import com.robinfinch.journal.app.R;

/**
 * Configuration.
 *
 * @author Mark Hoogenboom
 */
public class Config {

    private final Context context;

    public Config(Context context) {
        this.context = context;
    }

    public Uri getServerUri() {
        String server = context.getString(R.string.server);
        String port = context.getString(R.string.port);
        return Uri.parse("https://" + server + ":" + port + "/journal");
    }

    public String getGcmSenderId() {
        return context.getText(R.string.gcm_sender_id).toString();
    }
}
