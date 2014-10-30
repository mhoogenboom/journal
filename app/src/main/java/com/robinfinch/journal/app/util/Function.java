package com.robinfinch.journal.app.util;

/**
 * @author Mark Hoogenboom
 */
public interface Function<I, O> {
    O apply(I in);
}