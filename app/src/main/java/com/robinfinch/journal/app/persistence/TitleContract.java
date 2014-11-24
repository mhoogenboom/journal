package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliased;
import static com.robinfinch.journal.app.util.Utils.aliasedId;

/**
 * Data definition for {@link com.robinfinch.journal.domain.Title}.
 *
 * @author Mark Hoogenboom
 */
public interface TitleContract extends SyncableObjectContract {

    String NAME = "title";

    String COL_TITLE = "title";
    String COL_AUTHOR_ID = "author_id";
    String COL_YEAR = "year";

    String[] COLS = {
            aliasedId(NAME, COL_ID),
            aliased(NAME, COL_REMOTE_ID),
            aliased(NAME, COL_TITLE),
            aliased(NAME, COL_AUTHOR_ID),
            aliased(NAME, COL_YEAR),
            aliased(NAME, COL_LOG_ID),
            aliased(AuthorContract.NAME, AuthorContract.COL_ID),
            aliased(AuthorContract.NAME, AuthorContract.COL_REMOTE_ID),
            aliased(AuthorContract.NAME, AuthorContract.COL_NAME)
    };

    String JOINS = " LEFT JOIN " + AuthorContract.NAME + " ON (" + NAME + "." + COL_AUTHOR_ID + " = " + AuthorContract.NAME + "." + AuthorContract.COL_ID + ")";

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, JOINS);

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, JOINS);
}
