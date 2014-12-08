package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.ApplicationEntry;
import com.robinfinch.journal.domain.workflow.Action;
import com.robinfinch.journal.domain.workflow.Workflow;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Application entry list item view holder.
 *
 * @author Mark Hoogenboom
 */
class ApplicationEntryViewHolder {

    @InjectView(R.id.applicationentry_dayofentry)
    protected TextView dayOfEntryView;

    @InjectView(R.id.applicationentry_application)
    protected TextView applicationView;

    @InjectView(R.id.applicationentry_action)
    protected TextView actionView;

    private final Workflow workflow;

    public ApplicationEntryViewHolder(Workflow workflow, View view) {
        ButterKnife.inject(this, view);
        this.workflow = workflow;
    }

    public void bind(ApplicationEntry entry) {
        dayOfEntryView.setText(Formatter.formatDay(entry.getDayOfEntry()));

        applicationView.setText(Formatter.formatNamedObject(entry.getApplication()));

        Action action = workflow.getAction(entry.getActionId());
        if (action == null) {
            actionView.setText("");
        } else {
            actionView.setText(action.getDescription());
        }
    }
}
