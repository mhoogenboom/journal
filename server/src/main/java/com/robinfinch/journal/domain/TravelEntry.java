package com.robinfinch.journal.domain;

import com.robinfinch.journal.server.util.Formatter;

import javax.persistence.Entity;

/**
 * Journal entry describing a journey.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class TravelEntry extends JournalEntry {

    private boolean away; // away or back

    private String place;

    public boolean isAway() {
        return away;
    }

    public void setAway(boolean away) {
        this.away = away;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toPrettyString() {
        return "Journey " + Formatter.formatTravelDescription(away, place) + ".";
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.TravelEntry[id=" + getId()
                + ";owner=" + getOwner()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";away=" + isAway()
                + ";place=" + getPlace()
                + "]";
    }
}
