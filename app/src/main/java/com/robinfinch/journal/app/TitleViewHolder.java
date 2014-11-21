package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.Title;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author list item view holder.
 *
 * @author Mark Hoogenboom
 */
class TitleViewHolder {

    @InjectView(R.id.title_title)
    protected TextView titleView;

    @InjectView(R.id.title_author)
    protected TextView authorView;

    public TitleViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(Title title) {

        titleView.setText(title.getTitle());
        authorView.setText(Formatter.formatNamedObject(title.getAuthor()));
    }
}
