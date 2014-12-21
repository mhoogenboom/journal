package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliased;
import static com.robinfinch.journal.app.util.Utils.aliasedId;
import static com.robinfinch.journal.app.util.Utils.prefixed;

/**
 * Data definition for {@link com.robinfinch.journal.domain.Application}.
 *
 * @author Mark Hoogenboom
 */
public interface ApplicationContract extends SyncableObjectContract {

    String NAME = "application";

    String COL_RECRUITER_ID = "recruiter_id";
    String COL_CLIENT_ID = "client_id";
    String COL_START = "start";
    String COL_RATE = "rate";
    String COL_STATE_ID = "state_id";

    String[] COLS = {
            aliasedId(NAME, COL_ID),
            aliased(NAME, COL_ID),
            aliased(NAME, COL_REMOTE_ID),
            aliased(NAME, COL_RECRUITER_ID),
            aliased(NAME, COL_CLIENT_ID),
            aliased(NAME, COL_START),
            aliased(NAME, COL_RATE),
            aliased(NAME, COL_STATE_ID),
            aliased(NAME, COL_LOG_ID),
            aliased(RecruiterContract.NAME, RecruiterContract.COL_ID),
            aliased(RecruiterContract.NAME, RecruiterContract.COL_REMOTE_ID),
            aliased(RecruiterContract.NAME, RecruiterContract.COL_NAME),
            aliased(RecruiterContract.NAME, RecruiterContract.COL_ORGANISATION_ID),
            aliased(OrganisationContract.NAME, OrganisationContract.COL_ID),
            aliased(OrganisationContract.NAME, OrganisationContract.COL_REMOTE_ID),
            aliased(OrganisationContract.NAME, OrganisationContract.COL_NAME)
    };

    String JOINS = " LEFT JOIN " + RecruiterContract.NAME + " ON (" + prefixed(NAME, COL_RECRUITER_ID) + " = " + prefixed(RecruiterContract.NAME, RecruiterContract.COL_ID) + ")" +
                   " LEFT JOIN " + OrganisationContract.NAME + " ON (" + prefixed(NAME, COL_CLIENT_ID) + " = " + prefixed(OrganisationContract.NAME, OrganisationContract.COL_ID) + ")";

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, JOINS);

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, JOINS);
}
