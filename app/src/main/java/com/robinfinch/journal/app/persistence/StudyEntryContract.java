package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliased;
import static com.robinfinch.journal.app.util.Utils.aliasedId;

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
            aliased(NAME, COL_REMOTE_ID),
            COL_DAY_OF_ENTRY,
            aliased(NAME, COL_COURSE_ID),
            aliased(NAME, COL_DESCRIPTION),
            aliased(NAME, COL_LOG_ID),
            aliased(CourseContract.NAME, CourseContract.COL_ID),
            aliased(CourseContract.NAME, CourseContract.COL_REMOTE_ID),
            aliased(CourseContract.NAME, CourseContract.COL_NAME),
            aliased(CourseContract.NAME, CourseContract.COL_LOG_ID)
    };

    DirUriType DIR_URI_TYPE = new DirUriType(NAME,
            " LEFT JOIN " + CourseContract.NAME + " ON (" + NAME + "." + COL_COURSE_ID + " = " + CourseContract.NAME + "." + CourseContract.COL_ID + ")");

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME,
            " LEFT JOIN " + CourseContract.NAME + " ON (" + NAME + "." + COL_COURSE_ID + " = " + CourseContract.NAME + "." + CourseContract.COL_ID + ")");

}
