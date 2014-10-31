package com.robinfinch.journal.app.gcm;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.robinfinch.journal.app.persistence.MyContentProvider;
import com.robinfinch.journal.app.util.UriType;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Handler of GCM messages.
 *
 * @author Mark Hoogenboom
 */
public class GcmService extends IntentService {

    public GcmService() {
        super("gcmservice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        switch (messageType) {
            case GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE:
                handleTickle();
                break;
            case GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR:
                Log.w(LOG_TAG, "Gcm error " + intent.getExtras());
                break;
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void handleTickle() {
        Log.d(LOG_TAG, "Received tickle");

        Bundle extras = new Bundle();

        ContentResolver.requestSync(null, UriType.AUTHORITY, extras);
    }
}
