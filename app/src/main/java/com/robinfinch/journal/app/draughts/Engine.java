package com.robinfinch.journal.app.draughts;

import com.robinfinch.journal.domain.draughts.Move;
import com.robinfinch.journal.domain.draughts.Position;

/**
 * Draughts playing engine.
 *
 * @author Mark Hoogenboom
 */
public class Engine {

    static {
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("aeolus");
    }

    public native Move[] generate(Position position);
}
