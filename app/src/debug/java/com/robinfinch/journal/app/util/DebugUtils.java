package com.robinfinch.journal.app.util;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;

import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

public class DebugUtils {

    public static void riseAndShine(Activity activity) {
        activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);

        PowerManager power = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock lock =
                power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "wakeup!");
        lock.acquire();
        lock.release();
    }
}
