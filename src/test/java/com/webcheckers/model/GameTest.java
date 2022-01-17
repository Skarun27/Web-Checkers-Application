package com.webcheckers.ui.model;

import com.webcheckers.model.*;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.util.MoveValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
@Tag("Model-Tier")
/**
 * Operates a game
 * stores board and other data for the game
 */
public class GameTest {

    //attributes
    private String redPlayerName;
    private String whitePlayerName;
    private Piece.pieceColor activeColor;
    private Game game;
    private BoardView board;
    private Move move;
    private MoveValidationHelper helper;

    @BeforeEach
    public void testBuild(){
        redPlayerName="Tester";
        whitePlayerName="Opponent";
        activeColor= Piece.pieceColor.RED;
        board=mock(BoardView.class);
        move=mock(Move.class);
        helper=mock(MoveValidationHelper.class);
        game=new Game(board,redPlayerName,whitePlayerName,activeColor,helper);
    }

    @Test
    public void testGet(){
        assertEquals(redPlayerName,game.getRedPlayerName());
        assertEquals(activeColor,game.getActiveColor());
        assertEquals(whitePlayerName,game.getWhitePlayerName());
        assertEquals(board,game.getBoard());
        assertEquals(null,game.getGameEndMsg());
        assertEquals(null,game.getWinner());
        assertEquals(null,game.getMoveSequence());
    }
    @Test
    public void testendGame(){
        //test if the red player is out of pieces
        when(helper.piecesOfColor(board,Piece.pieceColor.RED)).thenReturn(0);
        game.checkEndGame();
        //test if the red player still has pieces, but the white player is out of pieces
        when(helper.piecesOfColor(board,Piece.pieceColor.RED)).thenReturn(1);
        when(helper.piecesOfColor(board,Piece.pieceColor.WHITE)).thenReturn(0);
        game.checkEndGame();
        //test if neither player is out of pieces
        when(helper.piecesOfColor(board,Piece.pieceColor.RED)).thenReturn(1);
        when(helper.piecesOfColor(board,Piece.pieceColor.WHITE)).thenReturn(1);
        game.checkEndGame();
        //test if neither player is out of pieces and it is white's turn
        game.changeActiveColor();
        when(helper.piecesOfColor(board,Piece.pieceColor.RED)).thenReturn(1);
        when(helper.piecesOfColor(board,Piece.pieceColor.WHITE)).thenReturn(0);
        game.checkEndGame();
    }
    @Test
    public void testGenerateMoves(){
        game.generateMovesList();
        activeColor= Piece.pieceColor.WHITE;
        game=new Game(board,redPlayerName,whitePlayerName,activeColor,helper);
        game.generateMovesList();
    }
    @Test
    public void testResign(){
        game.resign("Tester");
    }
    @Test
    public void testMakeMoves(){
        game.MakeMoves();
    }
    @Test
    public void testClearMoves(){
        game.clearMoves();
    }
    @Test
    public void testSetMoveSequence(){
        ArrayList<Move> moveSequence=mock(ArrayList.class);
        game.setMoveSequence(moveSequence);
    }
    @Test
    public void testAllMovesMade(){
        game.allMovesMade();
    }
    @Test
    public void testIncrement(){
        game.incrementMovesMade();
    }

}
