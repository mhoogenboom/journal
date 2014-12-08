package com.robinfinch.journal.domain.workflow;

/**
 * An action triggers the transition of a work item from one
 * {@link com.robinfinch.journal.domain.workflow.State state} to another.
 *
 * @author Mark Hoogenboom
 */
public class Action {

    private final long id;

    private final String description;

    Action(long id, String description) {
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
