package com.webcheckers.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaceTest {

//    defining the attributes
    private Space CuT;
    private Piece piece;

//    testing the constructor for generating a space without piece
    @Test
    public void spaceTest(){
        //testing the space creation with dark color as an example
        CuT = new Space(0, Space.SPACECOLOR.DARK);
        assertNotNull(CuT);
    }

//    testing the constructor for generating a space with piece
    @Test
    public void spaceTest2(){
        //testing the space creation with dark color and an arbitrary red piece as an example
        CuT = new Space(0, Space.SPACECOLOR.DARK, piece);
        piece = new Piece(Piece.Type.SINGLE, Piece.pieceColor.RED);
        assertNotNull(CuT);
    }

//    test setting and getting the color of a space
    @Test
    void getSpacecolorTest() {
//        assigning an arbitrary color to CuT and testing if getSpacecolor() returns the correct color
        CuT = new Space(0, Space.SPACECOLOR.DARK);
        assertEquals(CuT.getSpacecolor(), Space.SPACECOLOR.DARK);
    }

    @Test
    void getCellIdxTest() {
//        assigning an arbitrary cell index & color to CuT and testing if getCellIdx() returns the correct index
        CuT = new Space(4, Space.SPACECOLOR.LIGHT);
        assertEquals(CuT.getCellIdx(), 4);
    }

    @Test
    void getPieceTest() {
//        creating a space using space with piece constructor and test if the returned piece is equal to the piece
        piece = new Piece(Piece.Type.SINGLE, Piece.pieceColor.WHITE);
        CuT = new Space(0, Space.SPACECOLOR.DARK, piece);
        assertEquals(CuT.getPiece(), piece);
    }

    @Test
    void validMoveTest() {
//        checking the validMove() function
        piece = null;
        CuT = new Space(0, Space.SPACECOLOR.DARK, piece);
        assertEquals(CuT.validMove(), true);
        CuT = new Space(0, Space.SPACECOLOR.LIGHT, piece);
        assertEquals(CuT.validMove(), false);

    }
}