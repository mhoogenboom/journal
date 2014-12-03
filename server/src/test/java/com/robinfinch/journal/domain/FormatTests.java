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
        assertEquals("Studied.", entry.toPrettyString());

        entry.setCourse(philOfMind);
        assertEquals("Studied Philosophy of Mind.", entry.toPrettyString());

        entry.setDescription("unit 4.3");
        assertEquals("Studied Philosophy of Mind, unit 4.3.", entry.toPrettyString());

        entry.setCourse(null);
        assertEquals("Studied unit 4.3.", entry.toPrettyString());
    }

    @Test
    public void formatReadEntry() {

        Author ianMcEwan = new Author();
        ianMcEwan.setName("Ian McEwan");

        Title theChildrenAct = new Title();
        theChildrenAct.setTitle("The Children Act");

        ReadEntry entry = new ReadEntry();
        assertEquals("Read.", entry.toPrettyString());

        entry.setTitle(theChildrenAct);
        assertEquals("Read The Children Act.", entry.toPrettyString());

        theChildrenAct.setYear("2014");
        assertEquals("Read The Children Act (2014).", entry.toPrettyString());

        theChildrenAct.setAuthor(ianMcEwan);
        assertEquals("Read Ian McEwan: The Children Act (2014).", entry.toPrettyString());

        entry.setPart("chapter 1");
        assertEquals("Read Ian McEwan: The Children Act (2014), chapter 1.", entry.toPrettyString());
    }

    @Test
    public void formatWalkEntry() {

        WalkEntry entry = new WalkEntry();
        assertEquals("Walked.", entry.toPrettyString());

        entry.setLocation("Woodwalton Fen");
        assertEquals("Walked near Woodwalton Fen.", entry.toPrettyString());
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
        assertEquals("Travelled.", entry.toPrettyString());

        entry.setPlace("Penruddock");
        assertEquals("Travelled from Penruddock.", entry.toPrettyString());
    }
}
