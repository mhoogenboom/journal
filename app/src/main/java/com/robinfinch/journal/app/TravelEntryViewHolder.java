package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.TravelEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Travel entry list item view holder.
 *
 * @author Mark Hoogenboom
 */
class TravelEntryViewHolder {

    @InjectView(R.id.travelentry_dayoftravel)
    protected TextView dayOfEntryView;

    @InjectView(R.id.travelentry_description)
    protected TextView descriptionView;

    public TravelEntryViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(TravelEntry entry) {
        dayOfEntryView.setText(Formatter.formatDay(entry.getDayOfEntry()));
        descriptionView.setText(Formatter.formatTravelDescription(entry.isAway(), entry.getPlace()));
    }
}
