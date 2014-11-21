package com.robinfinch.journal.app;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Provides context dependency
 *
 * @author Mark Hoogenboom
 */
@Module(library=true)
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context providesContext() {
        return context;
    }
}
