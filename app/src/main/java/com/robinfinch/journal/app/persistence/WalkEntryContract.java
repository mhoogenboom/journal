package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliasedId;

/**
 * Data definition for {@link com.robinfinch.journal.domain.WalkEntry}.
 *
 * @author Mark Hoogenboom
 */
public interface WalkEntryContract extends JournalEntryContract {

    String NAME = "walkentry";

    String COL_LOCATION = "location";

    String[] COLS = {
            aliasedId(NAME, COL_ID), COL_REMOTE_ID, COL_DAY_OF_ENTRY, COL_LOCATION, COL_LOG_ID
    };

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, "");

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, "");
}

