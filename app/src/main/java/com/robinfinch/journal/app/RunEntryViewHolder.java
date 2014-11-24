package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.RunEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Run entry list item view holder.
 *
 * @author Mark Hoogenboom
 */
class RunEntryViewHolder {

    @InjectView(R.id.runentry_dayofrun)
    protected TextView dayOfEntryView;

    @InjectView(R.id.runentry_distance)
    protected TextView distanceView;

    @InjectView(R.id.runentry_timetaken)
    protected TextView timeTakenView;

    @InjectView(R.id.runentry_avgpace)
    protected TextView avgPaceView;

    public RunEntryViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(RunEntry entry) {
        dayOfEntryView.setText(Formatter.formatDay(entry.getDayOfEntry()));
        distanceView.setText(Formatter.formatDistance(entry.getDistance()));
        timeTakenView.setText(Formatter.formatTime(entry.getTimeTaken()));
        avgPaceView.setText(Formatter.formatPace(entry.getAvgPace()));
    }
}
