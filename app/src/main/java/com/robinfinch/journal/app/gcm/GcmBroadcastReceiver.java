package com.robinfinch.journal.app.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Receiver of GCM messages.
 *
 * @author Mark Hoogenboom
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        intent.setClass(context, GcmService.class);

        startWakefulService(context, intent);
        setResultCode(Activity.RESULT_OK);
    }
}