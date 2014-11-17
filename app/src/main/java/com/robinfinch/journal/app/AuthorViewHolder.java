package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.domain.Author;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author list item view holder.
 *
 * @author Mark Hoogenboom
 */
class AuthorViewHolder {

    @InjectView(R.id.author_name)
    protected TextView nameView;

    public AuthorViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(Author author) {

        nameView.setText(author.getName());
    }
}
