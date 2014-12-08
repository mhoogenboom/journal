package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.domain.Organisation;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Organisation list item view holder.
 *
 * @organisation Mark Hoogenboom
 */
class OrganisationViewHolder {

    @InjectView(R.id.organisation_name)
    protected TextView nameView;

    public OrganisationViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(Organisation organisation) {

        nameView.setText(organisation.getName());
    }
}
