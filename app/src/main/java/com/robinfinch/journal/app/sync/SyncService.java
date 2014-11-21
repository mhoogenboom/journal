package com.robinfinch.journal.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.robinfinch.journal.app.ContextModule;
import com.robinfinch.journal.app.notifications.NotificationModule;
import com.robinfinch.journal.app.persistence.PersistenceModule;
import com.robinfinch.journal.app.rest.ApiModule;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Service that provides an sync adapter.
 *
 * @author Mark Hoogenboom
 */
public class SyncService extends Service {

    @Inject
    SyncAdapter syncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

        ObjectGraph.create(
                new ContextModule(this),
                new SyncModule()
        ).inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}