package com.robinfinch.journal.app.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Checks if the connectivity is ok for sync.
 *
 * @author Mark Hoogenboom
 */
public class ConnectivityChecker {

    private final Context context;

    public ConnectivityChecker(Context context) {
        this.context = context;
    }

    public boolean isOk() {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getPackageName() + "_preferences", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);

        boolean onlyOverWifi = prefs.getBoolean("prefs_sync_only_over_wifi", false);

        boolean connectivityOk;
        if (onlyOverWifi) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            connectivityOk = networkInfo.isConnected();
        } else {
            connectivityOk = true;
        }

        return connectivityOk;
    }
}
