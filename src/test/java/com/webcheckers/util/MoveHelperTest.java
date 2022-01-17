package com.webcheckers.util;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.model.*;
import com.webcheckers.util.MoveValidationHelper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class MoveHelperTest {
    //attributes
    private BoardView board;
    private Move move;
    private Piece piece;
    private Space space;
    private Piece.pieceColor activeColor;
    private MoveValidationHelper moveValidationHelper;
    private ArrayList<Move> possibleMoves;
    private ArrayList<ArrayList<Move>> possibleJumps;


    @BeforeEach
    public void testBuild(){
        board=new BoardView();
        activeColor=Piece.pieceColor.RED;
        moveValidationHelper=new MoveValidationHelper();
        possibleMoves=mock(ArrayList.class);
        possibleJumps=mock(ArrayList.class);
        move=mock(Move.class);
    }

    @Test
    public void testPiecesOfColor(){
        moveValidationHelper.piecesOfColor(board,activeColor);

    }

    @Test
    public void testScanBoard(){
        moveValidationHelper.scanBoard(board,activeColor,possibleMoves,possibleJumps);
    }


}
