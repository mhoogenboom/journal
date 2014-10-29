package com.robinfinch.journal.domain;

import com.robinfinch.journal.server.util.Formatter;

import javax.persistence.Entity;

/**
 * Journal entry describing a run.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class RunEntry extends JournalEntry {

    private int distance; // in m

    private int timeTaken; // in s

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public String toPrettyString() {
        return "Run " + Formatter.formatDistance(distance)
                + " in " + Formatter.formatTime(timeTaken) + ".";
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.RunEntry[id=" + getId()
                + ";owner=" + getOwner()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";distance=" + getDistance()
                + ";timeTaken=" + getTimeTaken()
                + "]";
    }
}
