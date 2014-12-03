package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliased;
import static com.robinfinch.journal.app.util.Utils.aliasedId;

/**
 * Data definition for {@link com.robinfinch.journal.domain.ReadEntry}.
 *
 * @author Mark Hoogenboom
 */
public interface ReadEntryContract extends JournalEntryContract {

    String NAME = "readentry";

    String COL_TITLE_ID = "title_id";
    String COL_PART = "part";

    String[] COLS = {
            aliasedId(NAME, COL_ID),
            aliased(NAME, COL_REMOTE_ID),
            COL_DAY_OF_ENTRY,
            aliased(NAME, COL_TITLE_ID),
            aliased(NAME, COL_PART),
            aliased(NAME, COL_LOG_ID),
            aliased(TitleContract.NAME, TitleContract.COL_ID),
            aliased(TitleContract.NAME, TitleContract.COL_REMOTE_ID),
            aliased(TitleContract.NAME, TitleContract.COL_TITLE),
            aliased(TitleContract.NAME, TitleContract.COL_YEAR),
            aliased(TitleContract.NAME, TitleContract.COL_AUTHOR_ID),
            aliased(AuthorContract.NAME, AuthorContract.COL_ID),
            aliased(AuthorContract.NAME, AuthorContract.COL_REMOTE_ID),
            aliased(AuthorContract.NAME, AuthorContract.COL_NAME)
    };

    String JOINS =
            " LEFT JOIN " + TitleContract.NAME + " ON (" + NAME + "." + COL_TITLE_ID + " = " + TitleContract.NAME + "." + TitleContract.COL_ID + ")" +
            " LEFT JOIN " + AuthorContract.NAME + " ON (" + TitleContract.NAME + "." + TitleContract.COL_AUTHOR_ID + " = " + AuthorContract.NAME + "." + AuthorContract.COL_ID + ")";

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, JOINS);

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, JOINS);
}
