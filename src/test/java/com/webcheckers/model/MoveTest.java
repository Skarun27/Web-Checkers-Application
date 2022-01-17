package com.webcheckers.model;

import org.junit.Before;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class MoveTest {

    private Position begin;
    private Position end;
    private Move mov;

    @Test

    // Testing that a move has been started
    public void testingMoveIsStarted() {
        // setup testing environment
        begin = new Position(1, 2);
        end = new Position(3, 4);
        mov = new Move(begin, end);
        // assert
        assertNotNull(mov);

    }

    /**
     * Test getend method
     */
    @Test
    public void testGetEnd() {
        // setup testing environment
        begin = new Position(3, 4);
        end = new Position(4, 5);
        mov = new Move(begin, end);
        assertEquals(end, mov.getEnd());

    }


    /**
     * Test getStart method
     */
    @Test
    public void testGetStart() {
        // setup testing environment
        begin = new Position(3, 4);
        end = new Position(4, 5);
        mov = new Move(begin, end);
        assertEquals(begin, mov.getStart());


    }
}