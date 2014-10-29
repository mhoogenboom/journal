package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliasedId;

/**
 * Data definition for {@link com.robinfinch.journal.domain.TravelEntry}.
 *
 * @author Mark Hoogenboom
 */
public interface TravelEntryContract extends JournalEntryContract {

    String NAME = "travelentry";

    String COL_AWAY = "away";
    String COL_PLACE = "place";

    String[] COLS = {
            aliasedId(NAME, COL_ID), COL_REMOTE_ID, COL_DAY_OF_ENTRY, COL_AWAY, COL_PLACE, COL_LOG_ID
    };

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, "");

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, "");
}
