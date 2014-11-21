package com.robinfinch.journal.app.notifications;

import android.app.NotificationManager;
import android.content.Context;

import com.robinfinch.journal.app.MainActivity;
import com.robinfinch.journal.app.sync.SyncAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency module for notification dependencies
 *
 * @author Mark Hoogenboom
 */
@Module(
        injects={MainActivity.class, MyNotificationManager.class},
        complete=false
)
public class NotificationModule {

    @Provides
    public MyNotificationManager providesMyNotificationManager(Context context) {
        return new MyNotificationManager(context);
    }

    @Provides
    public NotificationManager providesNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
