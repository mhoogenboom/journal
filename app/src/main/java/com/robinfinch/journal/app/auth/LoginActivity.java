package com.robinfinch.journal.app.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.robinfinch.journal.app.R;
import com.robinfinch.journal.app.rest.JournalApi;
import com.robinfinch.journal.app.util.Config;
import com.robinfinch.journal.app.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

import static com.robinfinch.journal.app.util.Constants.*;

/**
 * A login screen that offers login via email/password.
 *
 * @author Mark Hoogenboom
 */
public class LoginActivity extends AccountAuthenticatorActivity implements LoaderCallbacks<Cursor> {

    private String accountType;
    private String authTokenType;

    private JournalApi api;

    private UserLoginTask authTask;

    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        accountType = getIntent().getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        authTokenType = getIntent().getStringExtra(ARG_AUTH_TOKEN_TYPE);

        Log.d(LOG_TAG, "Create login activity for " + accountType + " " + authTokenType);

        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        passwordView = (EditText) findViewById(R.id.password);

        Button signInButton = (Button) findViewById(R.id.login_go);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Config config = new Config(this);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(config.getServerUri().toString())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(JournalApi.class);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (authTask != null) {
            return;
        }

        emailView.setError(null);
        passwordView.setError(null);

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.login_error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.login_error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.login_error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            authTask = new UserLoginTask();
            authTask.execute(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailView.setAdapter(adapter);
    }

    public class UserLoginTask extends AsyncTask<String, Void, Intent> {

        @Override
        protected Intent doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            Log.d(LOG_TAG, "Sign in with " + email);

            String gcmRegistrationId = registerGcm();

            Intent res;
            try {
                String authToken = api.signin(email, Utils.hash(password, "MD5"), authTokenType, gcmRegistrationId);

                res = new Intent();
                res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                res.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
                res.putExtra(ARG_PASSWORD, password);
                res.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
            } catch (RetrofitError e) {
                Log.d(LOG_TAG, "Sign in failed", e);
                res = null;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Intent intent) {
            authTask = null;
            showProgress(false);

            if (intent == null) {
                passwordView.setError(getString(R.string.login_error_incorrect_password));
                passwordView.requestFocus();
            } else {
                finishLogin(intent);
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
            showProgress(false);
        }
    }

    private String registerGcm() {
        Config config = new Config(this);

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String gcmRegistrationId;
        try {
            gcmRegistrationId = gcm.register(config.getGcmSenderId());
        } catch (IOException e) {
            gcmRegistrationId = null;
            Log.e(LOG_TAG, "Failed to get GCM registration id", e);
        }
        return gcmRegistrationId;
    }

    private void finishLogin(Intent intent) {

        Account account = new Account(
                intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME),
                accountType);

        AccountManager accountManager = AccountManager.get(this);
        accountManager.addAccountExplicitly(account, null, null);
        accountManager.setAuthToken(account, authTokenType, intent.getStringExtra(AccountManager.KEY_AUTHTOKEN));

        Log.d(LOG_TAG, "Set authentication token for " + accountType + " " + authTokenType + " " + account.name);

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
