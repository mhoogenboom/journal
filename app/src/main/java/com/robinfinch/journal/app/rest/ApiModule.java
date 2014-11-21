package com.robinfinch.journal.app.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robinfinch.journal.app.auth.LoginActivity;
import com.robinfinch.journal.app.sync.SyncAdapter;
import com.robinfinch.journal.app.util.Config;

import java.lang.reflect.Type;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dependency module for api.
 */
@Module(
        injects={LoginActivity.class},
        complete=false
)
public class ApiModule {

    @Provides
    public JournalApi providesApi(Context context) {

        Config config = new Config(context);

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

        return restAdapter.create(JournalApi.class);
    }
}
