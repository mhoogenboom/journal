package com.robinfinch.journal.app.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import com.robinfinch.journal.app.ContextModule;
import com.robinfinch.journal.app.MainActivity;
import com.robinfinch.journal.app.R;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Manages notifications.
 *
 * @author Mark Hoogenboom
 */
public class MyNotificationManager {

    private static final int NOTIFICATION_SYNC_ID = 300;
    private static final int NOTIFICATION_OUT_OF_DATE_ID = 301;

    private final Context context;

    @Inject
    NotificationManager notificationManager;

    public MyNotificationManager(Context context) {
        this.context = context;

        ObjectGraph.create(
                new ContextModule(context),
                new NotificationModule()
        ).inject(this);
    }

    public void onMainActivityResumed() {

        notificationManager.cancel(NOTIFICATION_SYNC_ID);
    }

    public void onChangesReceived() {
        PendingIntent pendingIntent = MainActivity.intentFor(context);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getText(R.string.notification_sync_title))
                .setContentText(context.getText(R.string.notification_sync_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(NOTIFICATION_SYNC_ID, notification);
    }

    public void onClientOutOfDate() {

        PendingIntent pendingIntent = MainActivity.intentFor(context);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getText(R.string.notification_out_of_date_title))
                .setContentText(context.getText(R.string.notification_out_of_date_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(NOTIFICATION_OUT_OF_DATE_ID, notification);
    }
}
