package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.ReadEntry;
import com.robinfinch.journal.domain.Title;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Study entry list item view holder.
 *
 * @author Mark Hoogenboom
 */
class ReadEntryViewHolder {

    @InjectView(R.id.readentry_dayread)
    protected TextView dayOfEntryView;

    @InjectView(R.id.readentry_title)
    protected TextView titleView;

    @InjectView(R.id.readentry_part)
    protected TextView partView;

    @InjectView(R.id.readentry_author)
    protected TextView authorView;

    public ReadEntryViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(ReadEntry entry) {
        dayOfEntryView.setText(Formatter.formatDay(entry.getDayOfEntry()));

        Title title = entry.getTitle();
        if (title == null) {
            titleView.setVisibility(View.GONE);

            authorView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title.getTitle());

            authorView.setVisibility(View.VISIBLE);
            authorView.setText(Formatter.formatNamedObject(title.getAuthor()));
        }

        if (entry.getPart() == null) {
            partView.setVisibility(View.GONE);
        } else {
            partView.setVisibility(View.VISIBLE);
            partView.setText(entry.getPart());
        }
    }
}
