package com.robinfinch.journal.app.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Journal entry group list item view holder.
 *
 * @author Mark Hoogenboom
 */
class JournalEntryGroupViewHolder {

    @InjectView(R.id.year)
    protected TextView yearView;

    public JournalEntryGroupViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(JournalEntryGroup group) {

        yearView.setText("" + group.year);
    }
}
