package com.robinfinch.journal.app.sync;

import android.content.Context;
import android.net.ConnectivityManager;

import com.robinfinch.journal.app.notifications.NotificationModule;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency module for sync server.
 *
 * @author Mark Hoogenboom
 */
@Module(
        injects={SyncService.class, SyncAdapter.class, ConnectivityChecker.class},
        complete=false,
        includes = {NotificationModule.class}
)
public class SyncModule {

    @Provides
    public SyncAdapter providesSyncAdapter(Context context) {
        return new SyncAdapter(context, true);
    }

    @Provides
    public ConnectivityChecker providesConnectivityChecker(Context context) {
        return new ConnectivityChecker(context);
    }

    @Provides
    public ConnectivityManager providesConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
