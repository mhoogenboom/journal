package com.robinfinch.journal.domain;

/**
 * An object with an id and name.
 *
 * @author Mark Hoogenboom
 */
public interface NamedObject {

    Long getId();

    CharSequence getName();
}
