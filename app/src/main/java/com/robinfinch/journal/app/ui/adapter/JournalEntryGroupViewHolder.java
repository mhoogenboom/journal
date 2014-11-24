package com.robinfinch.journal.app.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.app.R;
import com.robinfinch.journal.app.util.Formatter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Journal entry group list item view holder.
 *
 * @author Mark Hoogenboom
 */
class JournalEntryGroupViewHolder {

    @InjectView(R.id.group)
    protected TextView groupView;

    public JournalEntryGroupViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(JournalEntryGroup group) {

        groupView.setText(Formatter.formatGroup(group));
    }
}
