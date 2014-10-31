package com.robinfinch.journal.app.persistence;

/**
 * Base class of data definitions for {@link com.robinfinch.journal.domain.SyncableObject}.
 *
 * @author Mark Hoogenboom
 */
public interface SyncableObjectContract {

    String COL_ID = "id";
    String COL_REMOTE_ID = "remote_id";
    String COL_LOG_ID = "log_id";
}
