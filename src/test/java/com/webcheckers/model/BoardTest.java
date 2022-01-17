package com.webcheckers.model;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.util.MoveValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
public class BoardTest {
    //attributes
    private BoardView board;
    private Move move;
    private Piece.pieceColor activeColor;
    private MoveValidationHelper helper;

    @BeforeEach
    public void testBuild(){
        activeColor= Piece.pieceColor.RED;
        move=mock(Move.class);
        helper=mock(MoveValidationHelper.class);
        board=new BoardView();

    }
  @Test
    public void testMakeMove(){
        //test simple move
        when(move.getStart()).thenReturn(new Position(1,2));
        when(move.getEnd()).thenReturn(new Position(2,2));
        board.makeMove(move,activeColor);
        //test becoming a king
      when(move.getStart()).thenReturn(new Position(1,4));
      when(move.getEnd()).thenReturn(new Position(0,2));
      board.makeMove(move,activeColor);
      when(move.getStart()).thenReturn(new Position(0,2));
      when(move.getEnd()).thenReturn(new Position(0,4));
      board.makeMove(move,activeColor);
    }

    @Test
    public void testJumps(){
        //set up jump
        when(move.getStart()).thenReturn(new Position(5,2));
        when(move.getEnd()).thenReturn(new Position(4,1));
        board.makeMove(move,activeColor);
        when(move.getStart()).thenReturn(new Position(2,3));
        when(move.getEnd()).thenReturn(new Position(3,2));
        board.makeMove(move,Piece.pieceColor.WHITE);
        when(move.getStart()).thenReturn(new Position(4,1));
        when(move.getEnd()).thenReturn(new Position(2,3));
        board.makeMove(move,activeColor);

    }

    @Test
    public void testIterator(){
        board.iterator();
    }
    @Test
    public void testReverseIterator(){
        board.reverseIterator();
    }
    @Test
    public void testGetSpace(){
        board.getSpace(0,0);
        board.getSpace(-1,0);
    }

}
