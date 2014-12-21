package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliased;
import static com.robinfinch.journal.app.util.Utils.aliasedId;
import static com.robinfinch.journal.app.util.Utils.prefixed;

/**
 * Data definition for {@link com.robinfinch.journal.domain.ApplicationEntry}.
 *
 * @organisation Mark Hoogenboom
 */
public interface ApplicationEntryContract extends JournalEntryContract {

    String NAME = "applicationentry";

    String COL_APPLICATION_ID = "application_id";
    String COL_ACTION_ID = "action_id";

    String[] COLS = {
            aliasedId(NAME, COL_ID),
            aliased(NAME, COL_ID),
            aliased(NAME, COL_REMOTE_ID),
            aliased(NAME, COL_DAY_OF_ENTRY),
            aliased(NAME, COL_APPLICATION_ID),
            aliased(NAME, COL_ACTION_ID),
            aliased(NAME, COL_LOG_ID),
            aliased(ApplicationContract.NAME, ApplicationContract.COL_ID),
            aliased(ApplicationContract.NAME, ApplicationContract.COL_REMOTE_ID),
            aliased(ApplicationContract.NAME, ApplicationContract.COL_CLIENT_ID),
            aliased(ApplicationContract.NAME, ApplicationContract.COL_START),
            aliased(ApplicationContract.NAME, ApplicationContract.COL_RATE),
            aliased(OrganisationContract.NAME, OrganisationContract.COL_ID),
            aliased(OrganisationContract.NAME, OrganisationContract.COL_REMOTE_ID),
            aliased(OrganisationContract.NAME, OrganisationContract.COL_NAME)
    };

    String JOINS =
            " LEFT JOIN " + ApplicationContract.NAME + " ON (" + prefixed(NAME, COL_APPLICATION_ID) + " = " + prefixed(ApplicationContract.NAME, ApplicationContract.COL_ID) + ")" +
            " LEFT JOIN " + OrganisationContract.NAME + " ON (" + prefixed(ApplicationContract.NAME, ApplicationContract.COL_CLIENT_ID) + " = " + prefixed(OrganisationContract.NAME, OrganisationContract.COL_ID) + ")";

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, JOINS);

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, JOINS);
}
