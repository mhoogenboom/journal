package com.robinfinch.journal.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.robinfinch.journal.app.notifications.MyNotificationManager;
import com.robinfinch.journal.app.settings.SettingsActivity;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;
import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Main activity.
 *
 * @author Mark Hoogenboom
 */
public class MainActivity extends Activity implements
        StudyEntryListFragment.Parent,
        StudyEntryFragment.Parent,
        WalkEntryListFragment.Parent,
        WalkEntryFragment.Parent,
        RunEntryListFragment.Parent,
        RunEntryFragment.Parent,
        TravelEntryListFragment.Parent,
        TravelEntryFragment.Parent,
        EmptyFragment.Parent {

    private boolean singlePaneLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView optionsList;
    private MyNotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Log.d(LOG_TAG, "Create main activity");

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        singlePaneLayout = (findViewById(R.id.container_details) == null);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ArrayAdapter<CharSequence> optionsAdapter = new ArrayAdapter<>(this, R.layout.option_list_item);
        optionsAdapter.add(getText(R.string.studyentries));
        optionsAdapter.add(getText(R.string.walkentries));
        optionsAdapter.add(getText(R.string.runentries));
        optionsAdapter.add(getText(R.string.travelentries));

        optionsList = (ListView) findViewById(R.id.navigation);
        optionsList.setAdapter(optionsAdapter);
        optionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionsList.setItemChecked(position, true);
                drawerLayout.closeDrawer(optionsList);
                onDrawerOptionSelected(optionsAdapter.getItem(position));
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.options_open, R.string.options_close) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        notificationManager = new MyNotificationManager(this);

        onDrawerOptionSelected(getText(R.string.studyentries));
    }

    private void onDrawerOptionSelected(Object option) {
        if (getText(R.string.studyentries).equals(option)) {
            showList(StudyEntryListFragment.newInstance());
        } else if (getText(R.string.walkentries).equals(option)) {
            showList(WalkEntryListFragment.newInstance());
        } else if (getText(R.string.runentries).equals(option)) {
            showList(RunEntryListFragment.newInstance());
        } else if (getText(R.string.travelentries).equals(option)) {
            showList(TravelEntryListFragment.newInstance());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean drawerClosed = !drawerLayout.isDrawerOpen(optionsList);

        setVisible(menu.findItem(R.id.action_settings), drawerClosed);

        return super.onPrepareOptionsMenu(menu);
    }

    private void setVisible(MenuItem item, boolean visible) {
        if (item != null) {
            item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        notificationManager.onMainActivityResumed();
    }

    @Override
    public void onStudyEntryItemSelected(Uri uri) {
        if (singlePaneLayout) {
            startDetailsActivity(StudyEntryActivity.class, uri);
        } else {
            showDetails(StudyEntryFragment.newInstance(uri));
        }
    }

    @Override
    public void onStudyEntryDeleted() {
        showDetails(EmptyFragment.newInstance());
    }

    @Override
    public void onWalkEntryItemSelected(Uri uri) {
        if (singlePaneLayout) {
            startDetailsActivity(WalkEntryActivity.class, uri);
        } else {
            showDetails(WalkEntryFragment.newInstance(uri));
        }
    }

    @Override
    public void onWalkEntryDeleted() {
        showDetails(EmptyFragment.newInstance());
    }

    @Override
    public void onRunEntryItemSelected(Uri uri) {
        if (singlePaneLayout) {
            startDetailsActivity(RunEntryActivity.class, uri);
        } else {
            showDetails(RunEntryFragment.newInstance(uri));
        }
    }

    @Override
    public void onRunEntryDeleted() {
        showDetails(EmptyFragment.newInstance());
    }

    @Override
    public void onTravelEntryItemSelected(Uri uri) {
        if (singlePaneLayout) {
            startDetailsActivity(TravelEntryActivity.class, uri);
        } else {
            showDetails(TravelEntryFragment.newInstance(uri));
        }
    }

    @Override
    public void onTravelEntryDeleted() {
        showDetails(EmptyFragment.newInstance());
    }

    private void showList(Fragment fragment) {
        if (!singlePaneLayout) {
            showDetails(EmptyFragment.newInstance());
        }
        getFragmentManager().beginTransaction().replace(R.id.container_list, fragment).commit();
    }

    private void showDetails(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.container_details, fragment).commit();
    }

    private void startDetailsActivity(Class<?> activityClass, Uri uri) {
        Intent intent = new Intent(this, activityClass)
                .putExtra(ARG_URI, uri);
        startActivity(intent);
    }
}
