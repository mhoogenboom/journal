package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliased;
import static com.robinfinch.journal.app.util.Utils.aliasedId;

/**
 * Data definition for {@link com.robinfinch.journal.domain.Organisation}.
 *
 * @organisation Mark Hoogenboom
 */
public interface OrganisationContract extends SyncableObjectContract {

    String NAME = "organisation";

    String COL_NAME = "name";

    String[] COLS = {
            aliasedId(NAME, COL_ID),
            aliased(NAME, COL_ID),
            aliased(NAME, COL_REMOTE_ID),
            aliased(NAME, COL_NAME),
            aliased(NAME, COL_LOG_ID)
    };

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, "");

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, "");
}

