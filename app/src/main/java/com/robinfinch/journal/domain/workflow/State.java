package com.robinfinch.journal.domain.workflow;

/**
 * State of a work item.
 *
 * Mark Hoogenboom
 */
public class State {

    private final long id;

    private final String description;

    State(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
