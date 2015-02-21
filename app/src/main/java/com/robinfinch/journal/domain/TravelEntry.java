package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.robinfinch.journal.app.persistence.TravelEntryContract;
import com.robinfinch.journal.app.util.Formatter;

import java.util.Date;

import static com.robinfinch.journal.app.util.Utils.alias;
import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Journal entry describing a journey.
 *
 * @author Mark Hoogenboom
 */
public class TravelEntry extends JournalEntry {

    private boolean away; // away or back

    private String place;

    public static TravelEntry from(Cursor cursor) {
        TravelEntry entry = new TravelEntry();
        int i;

        i = cursor.getColumnIndexOrThrow(alias(TravelEntryContract.NAME, TravelEntryContract.COL_ID));
        long id = cursor.getLong(i);
        entry.setId(id);

        i = cursor.getColumnIndexOrThrow(alias(TravelEntryContract.NAME, TravelEntryContract.COL_REMOTE_ID));
        long remoteId = cursor.getLong(i);
        entry.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(alias(TravelEntryContract.NAME, TravelEntryContract.COL_DAY_OF_ENTRY));
        long dayOfEntry = cursor.getLong(i);
        entry.setDayOfEntry((dayOfEntry == 0) ? null : new Date(dayOfEntry));

        i = cursor.getColumnIndexOrThrow(alias(TravelEntryContract.NAME, TravelEntryContract.COL_AWAY));
        boolean away = (cursor.getInt(i) == 1);
        entry.setAway(away);

        i = cursor.getColumnIndexOrThrow(alias(TravelEntryContract.NAME, TravelEntryContract.COL_PLACE));
        String place = cursor.getString(i);
        entry.setPlace(place);

        i = cursor.getColumnIndexOrThrow(alias(TravelEntryContract.NAME, TravelEntryContract.COL_LOG_ID));
        long logId = cursor.getLong(i);
        entry.setLogId(logId);

        return entry;
    }

    public boolean isAway() {
        return away;
    }

    public void setAway(boolean away) {
        if (differs(this.away, away)) {
            this.away = away;
            this.changed = true;
        }
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        if (differs(this.place, place)) {
            this.place = place;
            this.changed = true;
        }
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(TravelEntryContract.COL_DAY_OF_ENTRY, (getDayOfEntry() == null) ? 0 : getDayOfEntry().getTime());
        values.put(TravelEntryContract.COL_AWAY, away ? 1 : 0);
        values.put(TravelEntryContract.COL_PLACE, place);
        return values;
    }

    @Override
    public CharSequence toShareString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Travelled");
        if (!TextUtils.isEmpty(place)) {
            sb.append(" ");
            sb.append(Formatter.formatTravelDescription(away, place));
        }
        sb.append(".");
        return sb;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.TravelEntry[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";away=" + isAway()
                + ";place=" + getPlace()
                + ";logId=" + getLogId()
                + "]";
    }

}
