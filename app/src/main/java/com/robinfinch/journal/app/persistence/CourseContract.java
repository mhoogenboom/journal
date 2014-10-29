package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliasedId;

/**
 * Data definition for {@link com.robinfinch.journal.domain.Course}.
 *
 * @author Mark Hoogenboom
 */
public interface CourseContract extends SyncableObjectContract {

    String NAME = "course";

    String COL_NAME = "name";

    String[] COLS = {
            aliasedId(NAME, COL_ID), COL_REMOTE_ID, COL_NAME, COL_LOG_ID
    };

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, "");

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, "");
}

