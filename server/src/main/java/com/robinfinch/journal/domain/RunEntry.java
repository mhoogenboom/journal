package com.robinfinch.journal.domain;

import com.robinfinch.journal.server.util.Formatter;

import java.nio.charset.CharsetEncoder;

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
    public CharSequence toPrettyString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Run");
        if (distance > 0) {
            sb.append(" ");
            sb.append(Formatter.formatDistance(distance));
        }
        if (timeTaken > 0) {
            if (distance > 0) {
                sb.append(" in");
            }
            sb.append(" ");
            sb.append(Formatter.formatTime(timeTaken));
        }
        sb.append(".");
        return sb;
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
