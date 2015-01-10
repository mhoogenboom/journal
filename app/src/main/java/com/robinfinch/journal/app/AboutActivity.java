package com.robinfinch.journal.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * About activity.
 *
 * @author Mark Hoogenboom
 */
public class AboutActivity extends ActionBarActivity {

    @InjectView(R.id.about_container)
    WebView aboutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.about_activity);

        ButterKnife.inject(this);

        aboutContainer.loadUrl("file:///android_asset/about.html");
    }
}
