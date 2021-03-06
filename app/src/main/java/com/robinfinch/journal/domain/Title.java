package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.robinfinch.journal.app.persistence.AuthorContract;
import com.robinfinch.journal.app.persistence.TitleContract;

import static com.robinfinch.journal.app.util.Utils.alias;
import static com.robinfinch.journal.app.util.Utils.appendIfNotEmpty;
import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Written work.
 *
 * @author Mark Hoogenboom
 */
public class Title extends SyncableObject implements NamedObject {

    private String title;
    
    private long authorId;

    private transient Author author;

    private String year;

    public static Title from(Cursor cursor) {
        Title entry = new Title();
        int i;

        i = cursor.getColumnIndexOrThrow(alias(TitleContract.NAME, TitleContract.COL_ID));
        long id = cursor.getLong(i);
        entry.setId(id);

        i = cursor.getColumnIndexOrThrow(alias(TitleContract.NAME, TitleContract.COL_REMOTE_ID));
        long remoteId = cursor.getLong(i);
        entry.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(alias(TitleContract.NAME, TitleContract.COL_TITLE));
        String title = cursor.getString(i);
        entry.setTitle(title);

        i = cursor.getColumnIndexOrThrow(alias(TitleContract.NAME, TitleContract.COL_AUTHOR_ID));
        long authorId = cursor.getLong(i);
        if (authorId == 0) {
            entry.setAuthor(null);
        } else {
            Author author = Author.from(cursor);
            entry.setAuthor(author);
        }

        i = cursor.getColumnIndexOrThrow(alias(TitleContract.NAME, TitleContract.COL_YEAR));
        String year = cursor.getString(i);
        entry.setYear(year);

        i = cursor.getColumnIndex(alias(TitleContract.NAME, TitleContract.COL_LOG_ID));
        if (i != -1) {
            long logId = cursor.getLong(i);
            entry.setLogId(logId);
        }

        return entry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (differs(this.title, title)) {
            this.title = title;
            this.changed = true;
        }
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        if (differs(this.author, author)) {
            this.author = author;
            this.changed = true;
        }
    }

    public String getAuthorName() {
        return (author == null) ? null : author.getName();
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        if (differs(this.year, year)) {
            this.year = year;
            this.changed = true;
        }
    }

    @Override
    public CharSequence getName() {
        StringBuilder sb = new StringBuilder();
        if (title != null) {
            sb.append(title);
            appendIfNotEmpty(sb, " (", year, ")");
        }
        return sb;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(TitleContract.COL_TITLE, title);
        values.put(TitleContract.COL_AUTHOR_ID, (getAuthor() == null) ? 0 : getAuthor().getId());
        values.put(TitleContract.COL_YEAR, year);
        return values;
    }

    @Override
    public boolean prepareBeforeSend() {
        if (author == null) {
            authorId = 0;
        } else {
            if (author.getRemoteId() == 0) {
                return false;
            } else {
                authorId = author.getRemoteId();
            }
        }
        return super.prepareBeforeSend();
    }

    @Override
    public boolean prepareAfterReceive(SQLiteDatabase db) {
        if (authorId == 0) {
            author = null;
        } else {
            Cursor cursor = db.query(AuthorContract.NAME, AuthorContract.COLS,
                    alias(AuthorContract.NAME, AuthorContract.COL_REMOTE_ID) + "=" + authorId, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                author = Author.from(cursor);
            } else {
                return false;
            }
        }
        return super.prepareAfterReceive(db);
    }

    @Override
    public CharSequence toShareString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reading");
        appendIfNotEmpty(sb, " ", getAuthorName());
        if (!TextUtils.isEmpty(getAuthorName()) && !TextUtils.isEmpty(getName())) {
            sb.append(":");
        }
        appendIfNotEmpty(sb, " ", getName());
        sb.append(".");
        return sb;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Title[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";title=" + getTitle()
                + ";authorId=" + getAuthorId()
                + ";author=" + getAuthor()
                + ";year=" + getYear()
                + ";logId=" + getLogId()
                + "]";
    }
}
