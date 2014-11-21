package com.robinfinch.journal.app.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.robinfinch.journal.app.ContextModule;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Checks if the connectivity is ok for sync.
 *
 * @author Mark Hoogenboom
 */
public class ConnectivityChecker {

    private final Context context;

    @Inject
    ConnectivityManager connectivityManager;

    public ConnectivityChecker(Context context) {
        this.context = context;

        ObjectGraph.create(
                new ContextModule(context),
                new SyncModule()
        ).inject(this);
    }

    public boolean isOk() {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getPackageName() + "_preferences", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);

        boolean onlyOverWifi = prefs.getBoolean("prefs_sync_only_over_wifi", false);

        boolean connectivityOk;
        if (onlyOverWifi) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            connectivityOk = networkInfo.isConnected();
        } else {
            connectivityOk = true;
        }

        return connectivityOk;
    }
}
