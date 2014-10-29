package com.robinfinch.journal.app.persistence;

/**
 * Data definition for {@link com.robinfinch.journal.app.sync.Revision}.
 *
 * @author Mark Hoogenboom
 */
public interface RevisionContract {

    String NAME = "revision";

    String COL_LATEST_REVISION = "latest_revision";

    String[] COLS = {
            COL_LATEST_REVISION
    };
}

