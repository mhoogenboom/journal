package com.robinfinch.journal.app.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Service that provides an account authenticator.
 *
 * @author Mark Hoogenboom
 */
public class AuthenticatorService extends Service {

    private static Authenticator authenticator;

    @Override
    public void onCreate() {
        if (authenticator == null) {
            Log.d(LOG_TAG, "Create authenticator");

            authenticator = new Authenticator(getApplicationContext());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
