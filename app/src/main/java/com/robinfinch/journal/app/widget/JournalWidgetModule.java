package com.robinfinch.journal.app.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency module for journal widget service.
 *
 * @author Mark Hoogenboom
 */
@Module(
        injects={JournalWidgetService.class},
        complete=false
)
public class JournalWidgetModule {

    @Provides
    public AppWidgetManager providesAppWidgetManager(Context context) {
        return AppWidgetManager.getInstance(context);
    }
}
