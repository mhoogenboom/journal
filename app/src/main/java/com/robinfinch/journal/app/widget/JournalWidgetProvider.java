package com.robinfinch.journal.app.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;

import com.robinfinch.journal.app.MainActivity;
import com.robinfinch.journal.app.persistence.StudyEntryContract;

import static com.robinfinch.journal.app.util.Constants.ARG_WIDGET_IDS;

/**
 * Manages the home screen widget.
 *
 * @author Mark Hoogenboom
 */
public class JournalWidgetProvider extends AppWidgetProvider {

    public static final ComponentName NAME = new ComponentName(
            MainActivity.class.getPackage().getName(),
            JournalWidgetProvider.class.getName());

    private ContentObserver observer;

    @Override
    public void onEnabled(final Context context) {
        if (observer == null) {
            observer = new ContentObserver(null) {

                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(JournalWidgetProvider.NAME);

                    onUpdate(context, appWidgetManager, appWidgetIds);
                }
            };
            context.getContentResolver().registerContentObserver(StudyEntryContract.DIR_URI_TYPE.uri(context), true, observer);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Intent intent = new Intent(context, JournalWidgetService.class);
        intent.putExtra(ARG_WIDGET_IDS, appWidgetIds);
        context.startService(intent);
    }

    @Override
    public void onDisabled(Context context) {
        if (observer != null) {
            context.getContentResolver().unregisterContentObserver(observer);
            observer = null;
        }
    }
}

