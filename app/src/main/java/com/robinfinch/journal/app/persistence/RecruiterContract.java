package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliased;
import static com.robinfinch.journal.app.util.Utils.aliasedId;
import static com.robinfinch.journal.app.util.Utils.prefixed;

/**
 * Data definition for {@link com.robinfinch.journal.domain.Recruiter}.
 *
 * @organisation Mark Hoogenboom
 */
public interface RecruiterContract extends SyncableObjectContract {

    String NAME = "recruiter";

    String COL_NAME = "name";
    String COL_ORGANISATION_ID = "organisation_id";
    String COL_PHONE_NUMBER = "phone_number";

    String[] COLS = {
            aliasedId(NAME, COL_ID),
            aliased(NAME, COL_ID),
            aliased(NAME, COL_REMOTE_ID),
            aliased(NAME, COL_NAME),
            aliased(NAME, COL_ORGANISATION_ID),
            aliased(NAME, COL_PHONE_NUMBER),
            aliased(NAME, COL_LOG_ID),
            aliased(OrganisationContract.NAME, OrganisationContract.COL_ID),
            aliased(OrganisationContract.NAME, OrganisationContract.COL_REMOTE_ID),
            aliased(OrganisationContract.NAME, OrganisationContract.COL_NAME)
    };

    String JOINS = " LEFT JOIN " + OrganisationContract.NAME + " ON (" + prefixed(NAME, COL_ORGANISATION_ID) + " = " + prefixed(OrganisationContract.NAME, OrganisationContract.COL_ID) + ")";

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, JOINS);

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, JOINS);
}
