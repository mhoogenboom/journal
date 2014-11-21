package com.robinfinch.journal.app.gcm;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency module for gcm service.
 *
 * @author Mark Hoogenboom
 */
@Module(
        injects={GcmService.class},
        complete=false
)
public class GcmModule {

    @Provides
    public GoogleCloudMessaging provideGcm(Context context) {
        return GoogleCloudMessaging.getInstance(context);
    }
}

