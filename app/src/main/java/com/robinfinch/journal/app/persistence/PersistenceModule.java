package com.robinfinch.journal.app.persistence;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency module for content provider.
 */
@Module(
        injects={MyContentProvider.class},
        complete=false
)
public class PersistenceModule {

    @Provides
    public DbHelper providesDbHelper(Context context) {
        return new DbHelper(context);
    }
}
