package com.robinfinch.journal.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robinfinch.journal.app.notifications.MyNotificationManager;
import com.robinfinch.journal.app.rest.DiffResponse;
import com.robinfinch.journal.app.rest.JournalApi;
import com.robinfinch.journal.app.rest.SyncableObjectListDeserializer;
import com.robinfinch.journal.app.rest.SyncableObjectWrapper;
import com.robinfinch.journal.app.rest.SyncableObjectWrapperSerializer;
import com.robinfinch.journal.app.util.Config;

import org.apache.http.conn.ClientConnectionManager;

import java.lang.reflect.Type;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Service that provides an sync adapter.
 *
 * @author Mark Hoogenboom
 */
public class SyncService extends Service {

    private static final int MAX_ENTRIES_TO_SEND = 50;

    private static SyncAdapter syncAdapter;

    @Override
    public void onCreate() {
        if (syncAdapter == null) {
            Log.d(LOG_TAG, "Create sync adapter");

            Config config = new Config(this);

            ConnectivityChecker connectivityChecker = new ConnectivityChecker(getApplicationContext());

            Type t;
            try {
                t = DiffResponse.class.getDeclaredMethod("getChanges").getGenericReturnType();
            } catch (NoSuchMethodException e) {
                t = null;
            }

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .registerTypeAdapter(SyncableObjectWrapper.class, new SyncableObjectWrapperSerializer())
                    .registerTypeAdapter(t, new SyncableObjectListDeserializer())
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(config.getServerUri().toString())
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            JournalApi api = restAdapter.create(JournalApi.class);

            MyNotificationManager connectionManager = new MyNotificationManager(getApplicationContext());

            syncAdapter = new SyncAdapter(getApplicationContext(), true)
                    .config(connectivityChecker, api, MAX_ENTRIES_TO_SEND, connectionManager);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}