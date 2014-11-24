package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.WalkEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Walk entry list item view holder.
 *
 * @author Mark Hoogenboom
 */
class WalkEntryViewHolder {

    @InjectView(R.id.walkentry_dayofwalk)
    protected TextView dayOfEntryView;

    @InjectView(R.id.walkentry_description)
    protected TextView descriptionView;

    public WalkEntryViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(WalkEntry entry) {
        dayOfEntryView.setText(Formatter.formatDay(entry.getDayOfEntry()));
        descriptionView.setText(Formatter.formatWalkDescription(entry.getLocation()));
    }
}
