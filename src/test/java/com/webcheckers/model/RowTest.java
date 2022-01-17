package com.webcheckers.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RowTest {
    Row rw ;


    @Test
    // testing getSpace method
    public void getSpace() {
        rw = new Row(1, Piece.pieceColor.RED);
        assertEquals(7,rw.getSpace(7).getCellIdx());
    }

    //testing getIndex method
    @Test
    public void getIndex(){
        //assertNotNull(rw.getIndex());
        //setting test environment
        rw = new Row(8, Piece.pieceColor.RED);
        assertEquals(8,rw.getIndex());
    }

    //testing iterator
    @Test
    public void Iterator(){
        //set up the environment
        rw = new Row(8, Piece.pieceColor.WHITE);
        assertNotNull(rw.iterator());

    }

    //testing hasNext
    @Test
    public void hasNext(){
        //set up the environment
        rw = new Row(8, Piece.pieceColor.WHITE);
        assertTrue(rw.iterator().hasNext());

    }

    //testing replaceSpace method
    @Test
    public void replaceSpace(){
        //set up the environment
        rw = new Row(8, Piece.pieceColor.WHITE);
        assertNotNull(rw.iterator());

    }

}