package com.robinfinch.journal.app.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import com.robinfinch.journal.app.MainActivity;
import com.robinfinch.journal.app.R;

/**
 * Manages notifications.
 *
 * @author Mark Hoogenboom
 */
public class MyNotificationManager {

    private static final int NOTIFICATION_ID = 300;

    private final Context context;

    public MyNotificationManager(Context context) {
        this.context = context;
    }

    public void onMainActivityResumed() {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void onChangesReceived() {
        PendingIntent pendingIntent = MainActivity.intentFor(context);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getText(R.string.sync_notification_title))
                .setContentText(context.getText(R.string.sync_notification_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
