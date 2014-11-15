package com.robinfinch.journal.app.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import com.robinfinch.journal.app.MainActivity;
import com.robinfinch.journal.app.R;
import com.robinfinch.journal.app.StudyEntryRemoteViewHolder;
import com.robinfinch.journal.app.persistence.StudyEntryContract;
import com.robinfinch.journal.app.util.Constants;
import com.robinfinch.journal.domain.StudyEntry;

import java.util.Arrays;
import java.util.Date;

import static com.robinfinch.journal.app.util.Constants.*;

/**
 * Updates widgets in the background.
 *
 * @author Mark Hoogenboom
 */
public class JournalWidgetService extends IntentService {

    public JournalWidgetService() {
        super("JournalWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());

        int[] widgetIds = intent.getIntArrayExtra(ARG_WIDGET_IDS);

        Cursor cursor = getContentResolver().query(
                StudyEntryContract.DIR_URI_TYPE.uri(),
                StudyEntryContract.COLS, null, null,
                StudyEntryContract.COL_DAY_OF_ENTRY + " DESC LIMIT 1");
        if (cursor.moveToNext()) {
            StudyEntry entry = StudyEntry.from(cursor, StudyEntryContract.NAME + "_");

            Log.d(LOG_TAG, "Updating widgets " + Arrays.toString(widgetIds) + " with " + entry);

            for (int widgetId : widgetIds) {
                PendingIntent pendingIntent = MainActivity.intentFor(getApplicationContext());

                RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget);
                views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

                StudyEntryRemoteViewHolder viewHolder = new StudyEntryRemoteViewHolder(views);
                viewHolder.bind(entry);

                manager.updateAppWidget(widgetId, views);
            }
        } else {
            Log.d(LOG_TAG, "Not updating widgets, no data");
        }
    }
}
