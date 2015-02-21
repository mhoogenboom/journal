package com.robinfinch.journal.app.util;

/**
 *
 * @author Mark Hoogenboom
 */
public abstract class UriType {

    private final String entityName;
    private final String joinedEntities;

    public UriType(String entityName, String joinedEntities) {
        this.entityName = entityName;
        this.joinedEntities = joinedEntities;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getJoinedEntities() {
        return joinedEntities;
    }

    public abstract String match();
}
