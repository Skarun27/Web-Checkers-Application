package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;


/**
 *
 * @author rk4850@rit.edu
 */
class PieceTest {
    // mock objects
    private Piece place;

    /**
     * Initialize  mock piece.
     */
    @Before
    public void startPiece() throws Exception {
        this.place = mock(Piece.class);

    }

    /**
     * Test the Type functionality of the Piece
     */
    @Test
    void getType() {
        place = new Piece(Piece.Type.SINGLE, Piece.pieceColor.WHITE);
        assertEquals(Piece.Type.SINGLE,place.getType());
    }
    /**
     * Test the color functionality of the Piece
     */
    @Test
    void getColor() {
        place = new Piece(Piece.Type.SINGLE, Piece.pieceColor.WHITE);
        assertEquals(Piece.pieceColor.WHITE,place.getColor());
    }
}