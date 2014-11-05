package com.robinfinch.journal.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests text representations of {@link com.robinfinch.journal.domain.JournalEntry journal entries}.
 *
 * @author Mark Hoogenboom
 */
public class FormatTests {

    @Test
    public void formatStudyEntry() {

        Course philOfMind = new Course();
        philOfMind.setName("Philosophy of Mind");

        StudyEntry entry = new StudyEntry();
        assertEquals(".", entry.toPrettyString());

        entry.setCourse(philOfMind);
        assertEquals("Philosophy of Mind.", entry.toPrettyString());

        entry.setDescription("Unit 4.3");
        assertEquals("Philosophy of Mind, Unit 4.3.", entry.toPrettyString());

        entry.setCourse(null);
        assertEquals("Unit 4.3.", entry.toPrettyString());
    }

    @Test
    public void formatWalkEntry() {

        WalkEntry entry = new WalkEntry();
        assertEquals("Walk.", entry.toPrettyString());

        entry.setLocation("Woodwalton Fen");
        assertEquals("Walk near Woodwalton Fen.", entry.toPrettyString());
    }

    @Test
    public void formatRunEntry() {

        RunEntry entry = new RunEntry();
        assertEquals("Run.", entry.toPrettyString());

        entry.setDistance(5300);
        assertEquals("Run 5.3 km.", entry.toPrettyString());

        entry.setTimeTaken(25 * 60 + 3);
        assertEquals("Run 5.3 km in 25:03.", entry.toPrettyString());

        entry.setDistance(0);
        assertEquals("Run 25:03.", entry.toPrettyString());
    }

    @Test
    public void formatTravelEntry() {

        TravelEntry entry = new TravelEntry();
        assertEquals("Journey.", entry.toPrettyString());

        entry.setPlace("Penruddock");
        assertEquals("Journey from Penruddock.", entry.toPrettyString());
    }
}
