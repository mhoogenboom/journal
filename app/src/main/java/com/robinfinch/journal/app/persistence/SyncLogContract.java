package com.robinfinch.journal.app.persistence;

/**
 * Data definition for {@link com.robinfinch.journal.domain.SyncLog}.
 *
 * @author Mark Hoogenboom
 */
public interface SyncLogContract {

    String NAME = "synclog";

    String COL_ID = "_id";
    String COL_ENTITY_NAME = "entity_name";
    String COL_ENTITY_ID = "entity_id";
    String COL_ENTITY_REMOTE_ID = "entity_remote_id";

    String[] COLS = {
            COL_ID, COL_ENTITY_NAME, COL_ENTITY_ID, COL_ENTITY_REMOTE_ID
    };
}

