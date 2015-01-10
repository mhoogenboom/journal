package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.robinfinch.journal.app.persistence.AuthorContract;

import static com.robinfinch.journal.app.util.Utils.alias;
import static com.robinfinch.journal.app.util.Utils.appendIfNotEmpty;
import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Author of a {@link com.robinfinch.journal.domain.Title}.
 *
 * @author Mark Hoogenboom
 */
public class Author extends SyncableObject implements NamedObject {

    private String name;

    public static Author from(Cursor cursor) {
        Author author = new Author();
        int i;

        i = cursor.getColumnIndexOrThrow(alias(AuthorContract.NAME, AuthorContract.COL_ID));
        long id = cursor.getLong(i);
        author.setId(id);

        i = cursor.getColumnIndexOrThrow(alias(AuthorContract.NAME, AuthorContract.COL_REMOTE_ID));
        long remoteId = cursor.getLong(i);
        author.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(alias(AuthorContract.NAME, AuthorContract.COL_NAME));
        String name = cursor.getString(i);
        author.setName(name);

        i = cursor.getColumnIndex(alias(AuthorContract.NAME, AuthorContract.COL_LOG_ID));
        if (i != -1) {
            long logId = cursor.getLong(i);
            author.setLogId(logId);
        }

        return author;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (differs(this.name, name)) {
            this.name = name;
            this.changed = true;
        }
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(AuthorContract.COL_NAME, getName());
        return values;
    }

    @Override
    public CharSequence toShareString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reading");
        appendIfNotEmpty(sb, " ", getName());
        sb.append(".");
        return sb;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Author[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";name=" + getName()
                + ";logId=" + getLogId()
                + "]";
    }
}
