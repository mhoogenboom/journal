package com.robinfinch.journal.app.dabbler;

import android.content.Context;
import android.content.res.Resources;

import com.robinfinch.journal.app.DabbleFragment;
import com.robinfinch.journal.app.R;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency module for sync server.
 *
 * @author Mark Hoogenboom
 */
@Module(
        injects={DabbleFragment.class},
        complete=false
)
public class DabblerModule {

    @Provides
    public AndroidPainter providesPainter(Context context) {
        Resources res = context.getResources();
        return new AndroidPainter(
                res.getDimensionPixelSize(R.dimen.dabble_width),
                res.getDimensionPixelSize(R.dimen.dabble_height));
    }
}
