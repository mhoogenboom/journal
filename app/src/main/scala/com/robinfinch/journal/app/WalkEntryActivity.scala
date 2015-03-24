package com.robinfinch.journal.app;

import android.net.Uri
import com.robinfinch.journal.app.ui.DetailsActivity

/**
 * Walk entry details activity.
 *
 * @author Mark Hoogenboom
 */
class WalkEntryActivity extends DetailsActivity with WalkEntryFragment.Parent {

    override def newFragmentFor(uri : Uri) = WalkEntryFragment.newInstance(uri)

    override def onWalkEntryDeleted() {
        finish();
    }
}
