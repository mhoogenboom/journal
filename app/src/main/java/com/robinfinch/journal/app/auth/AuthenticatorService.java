package com.robinfinch.journal.app.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.robinfinch.journal.app.ContextModule;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Service that provides an account authenticator.
 *
 * @author Mark Hoogenboom
 */
public class AuthenticatorService extends Service {

    @Inject
    Authenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();

        ObjectGraph.create(
                new ContextModule(this),
                new AuthenticatorModule()
        ).inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
