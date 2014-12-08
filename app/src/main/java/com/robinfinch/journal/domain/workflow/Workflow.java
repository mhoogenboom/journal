package com.robinfinch.journal.domain.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A workflow defines the transitions of work items from one
 * {@link com.robinfinch.journal.domain.workflow.State state} to another.
 *
 * @author Mark Hoogenboom
 */
public abstract class Workflow {

    private State initialState;
    private final List<Action> actions;
    private final Map<State, Map<Action, State>> transitions;

    protected Workflow() {
        actions = new ArrayList<>();
        transitions = new HashMap<>();
    }

    protected State defineInitialState(long id, String description) {
        initialState = defineState(id, description);
        return initialState;
    }

    protected State defineState(long id, String description) {
        State state = new State(id, description);
        Map<Action, State> actions = new HashMap<>();
        transitions.put(state, actions);
        return state;
    }

    protected Action defineAction(long id, String description) {
        Action action = new Action(id, description);
        actions.add(action);
        return action;
    }

    protected void defineTransition(State currentState, Action action, State nextState) {
        assert (currentState != null) && (action != null) & (nextState != null);

        transitions(currentState).put(action, nextState);
    }

    public State getInitialState() {
        return initialState;
    }

    public State getState(long id) {
        for (State state : transitions.keySet()) {
            if (state.getId() == id) {
                return state;
            }
        }
        return null;
    }

    public Collection<Action> getActions(State state) {
        assert (state != null);

        return transitions(state).keySet();
    }

    public Action getAction(long id) {
        for (Action action : actions) {
            if (action.getId() == id) {
                return action;
            }
        }
        return null;
    }

    public State getNextState(State currentState, Action action) {
        assert (currentState != null) && (action != null);

        return transitions(currentState).get(action);
    }

    private Map<Action, State> transitions(State state) {
        Map<Action, State> actions = transitions.get(state);
        if (actions == null) {
            throw new IllegalArgumentException("State is undefined");
        }
        return actions;
    }
}
