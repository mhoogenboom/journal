package com.robinfinch.journal.app.auth;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency module for authenticator service.
 *
 * @author Mark Hoogenboom
 */
@Module(
        injects={AuthenticatorService.class},
        complete=false
)
public class AuthenticatorModule {

    @Provides
    public Authenticator provideAuthenticator(Context context) {
        return new Authenticator(context);
    }
}
