package com.robinfinch.journal.app.test;

import android.test.AndroidTestCase;

import com.robinfinch.journal.app.draughts.Engine;
import com.robinfinch.journal.domain.draughts.Move;
import com.robinfinch.journal.domain.draughts.Position;

import static com.robinfinch.journal.domain.draughts.Position.BK;
import static com.robinfinch.journal.domain.draughts.Position.BM;
import static com.robinfinch.journal.domain.draughts.Position.EMPTY;
import static com.robinfinch.journal.domain.draughts.Position.WK;
import static com.robinfinch.journal.domain.draughts.Position.WM;

/**
 * Tests draughts engine.
 *
 * @author Mark Hoogenboom
 */
public class EngineTest extends AndroidTestCase {

    private Engine engine;

    @Override
    public void setUp() {
        engine = new Engine();
    }

    public void testManMoves1() {
        // move left to an empty square, not right off the edge or to an occupied square
        int[] position = {
                WM,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, BM, EMPTY,
                EMPTY, EMPTY, BM, EMPTY, EMPTY,
                EMPTY, EMPTY, WM, EMPTY, EMPTY,
                EMPTY, WM, EMPTY, EMPTY, WM,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
        };

        Move[] moves = engine.generate(new Position(position));

        assertEquals(3, moves.length);
        assertEquals("Move[path=[28, 22]]", moves[0].toString());
        assertEquals("Move[path=[32, 27]]", moves[1].toString());
        assertEquals("Move[path=[35, 30]]", moves[2].toString());
    }

    public void testManMoves2() {
        // move right to an empty square, not left off the edge or to an occupied square
        int[] position = {
                WM,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, BM, EMPTY, EMPTY,
                EMPTY, EMPTY, BM, EMPTY, EMPTY,
                WM, EMPTY, EMPTY, WM, EMPTY,
                EMPTY, EMPTY, EMPTY, WM, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
        };

        Move[] moves = engine.generate(new Position(position));

        assertEquals(3, moves.length);
        assertEquals("Move[path=[26, 21]]", moves[0].toString());
        assertEquals("Move[path=[29, 24]]", moves[1].toString());
        assertEquals("Move[path=[34, 30]]", moves[2].toString());
    }

    public void testManJumps() {
        // pass empty squares more than once, but don't jump opponent pieces more than once
        int[] position = {
                WM,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                BM, BK, BM, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                BM, BM, BM, WM, EMPTY,
                WM, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, BK, BM, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
        };

        Move[] moves = engine.generate(new Position(position));

        assertEquals(3, moves.length);
        assertEquals("Move[path=[26, 21, 17, 12, 8, 13, 19, 23, 28, 33, 39, 34, 30]]", moves[0].toString());
        assertEquals("Move[path=[26, 21, 17, 12, 8, 13, 19, 23, 28, 22, 17, 11, 6]]", moves[1].toString());
        assertEquals("Move[path=[26, 21, 17, 22, 28, 23, 19, 13, 8, 12, 17, 11, 6]]", moves[2].toString());
    }


    public void testKingMoves() {
        // move in four directions to an empty square, not off the edge or to an occupied square
        int[] position = {
                WM,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                BM, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, WK, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, WM,
        };

        Move[] moves = engine.generate(new Position(position));

        assertEquals(17, moves.length);
        assertEquals("Move[path=[28, 22]]", moves[0].toString());
        assertEquals("Move[path=[28, 17]]", moves[1].toString());
        assertEquals("Move[path=[28, 11]]", moves[2].toString());
        assertEquals("Move[path=[28, 23]]", moves[3].toString());
        assertEquals("Move[path=[28, 19]]", moves[4].toString());
        assertEquals("Move[path=[28, 14]]", moves[5].toString());
        assertEquals("Move[path=[28, 10]]", moves[6].toString());
        assertEquals("Move[path=[28, 5]]", moves[7].toString());
        assertEquals("Move[path=[28, 33]]", moves[8].toString());
        assertEquals("Move[path=[28, 39]]", moves[9].toString());
        assertEquals("Move[path=[28, 44]]", moves[10].toString());
        assertEquals("Move[path=[28, 32]]", moves[11].toString());
        assertEquals("Move[path=[28, 37]]", moves[12].toString());
        assertEquals("Move[path=[28, 41]]", moves[13].toString());
        assertEquals("Move[path=[28, 46]]", moves[14].toString());
        assertEquals("Move[path=[50, 44]]", moves[15].toString());
        assertEquals("Move[path=[50, 45]]", moves[16].toString());
    }

    public void testKingJumps() {
        // pass empty squares more than once, but don't jump opponent pieces more than once
        int[] position = {
                WM,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, BM, EMPTY, EMPTY, EMPTY,
                EMPTY, BK, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, BM, BM, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, BM, BM, EMPTY, EMPTY,
                BM, EMPTY, EMPTY, EMPTY, WK,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
        };

        Move[] moves = engine.generate(new Position(position));

        assertEquals(3, moves.length);
        assertEquals("Move[path=[35, 19, 8, 12, 21, 27, 32, 28, 23]]", moves[0].toString());
        assertEquals("Move[path=[35, 19, 8, 12, 26, 31, 37, 28, 23]]", moves[1].toString());
        assertEquals("Move[path=[35, 19, 2, 7, 16, 27, 32, 28, 23]]", moves[2].toString());
    }

    @Override
    public void tearDown() {
        engine = null;
    }
}
