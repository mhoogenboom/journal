package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliased;
import static com.robinfinch.journal.app.util.Utils.aliasedId;
import static com.robinfinch.journal.app.util.Utils.prefixed;

/**
 * Data definition for {@link com.robinfinch.journal.domain.StudyEntry}.
 *
 * @author Mark Hoogenboom
 */
public interface StudyEntryContract extends JournalEntryContract {

    String NAME = "studyentry";

    String COL_COURSE_ID = "course_id";
    String COL_DESCRIPTION = "description";

    String[] COLS = {
            aliasedId(NAME, COL_ID),
            aliased(NAME, COL_ID),
            aliased(NAME, COL_REMOTE_ID),
            aliased(NAME, COL_DAY_OF_ENTRY),
            aliased(NAME, COL_COURSE_ID),
            aliased(NAME, COL_DESCRIPTION),
            aliased(NAME, COL_LOG_ID),
            aliased(CourseContract.NAME, CourseContract.COL_ID),
            aliased(CourseContract.NAME, CourseContract.COL_REMOTE_ID),
            aliased(CourseContract.NAME, CourseContract.COL_NAME)
    };

    String JOINS = " LEFT JOIN " + CourseContract.NAME + " ON (" + prefixed(NAME, COL_COURSE_ID) + " = " + prefixed(CourseContract.NAME, CourseContract.COL_ID) + ")";

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, JOINS);

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, JOINS);
}
