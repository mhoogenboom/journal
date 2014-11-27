package com.robinfinch.journal.app.rest;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robinfinch.journal.app.R;
import com.robinfinch.journal.app.auth.LoginActivity;
import com.robinfinch.journal.app.util.Config;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

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

        SSLContext sslContext = createSslContext(context, "journal".toCharArray());

        OkHttpClient sslClient = new OkHttpClient();
        if (sslContext != null) {
            sslClient.setSslSocketFactory(sslContext.getSocketFactory());
        }

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
                .setClient(new OkClient(sslClient))
                .setEndpoint(config.getServerUri().toString())
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return restAdapter.create(JournalApi.class);
    }

    private SSLContext createSslContext(Context context, char[] password) {
        Log.d(LOG_TAG, "Creating SSL context");

        try {
            KeyStore keystore = KeyStore.getInstance("BKS");

            InputStream in = context.getResources().openRawResource(R.raw.journal);
            keystore.load(in, password);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, password);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            return sslContext;
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException
                | UnrecoverableKeyException | KeyManagementException | IOException e) {
            Log.w(LOG_TAG, "Failed to create SSL context", e);
            return null;
        }
    }
}
