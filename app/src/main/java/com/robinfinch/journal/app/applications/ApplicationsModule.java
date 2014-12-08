package com.robinfinch.journal.app.applications;

import android.content.Context;

import com.robinfinch.journal.app.ApplicationEntryListFragment;
import com.robinfinch.journal.app.ApplicationFragment;
import com.robinfinch.journal.domain.workflow.Workflow;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency module for applications.
 *
 * @author Mark Hoogenboom
 */
@Module(
        injects={ApplicationEntryListFragment.class, ApplicationFragment.class},
        complete=false
)
public class ApplicationsModule {

    @Provides
    public Workflow providesWorkflow(Context context) {
        return new ApplicationWorkflow();
    }
}
