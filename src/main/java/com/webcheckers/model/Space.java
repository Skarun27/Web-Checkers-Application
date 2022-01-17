package com.webcheckers.model;

public class Space {

    /**
     * Enum for color of the spaces
     */
    public enum SPACECOLOR{
        LIGHT,
        DARK
    }

    //attributes
    private int cellIdx;
    private Piece piece;
    private SPACECOLOR spacecolor;

    /**
     * Constructor for initializing spaces without piece
     * @param cellIdx - index of the space
     * @param spacecolor - color of space created
     */
    public Space(int cellIdx, SPACECOLOR spacecolor) {
        this.cellIdx = cellIdx;
        this.spacecolor = spacecolor;
    }

    /**
     * Constructor for initializing spaces with pieces
     * @param cellIdx - index of the space
     * @param spaceColor - color of space created
     */
    public Space(int cellIdx, SPACECOLOR spaceColor, Piece piece){
        this.cellIdx = cellIdx;
        this.piece = piece;
        this.spacecolor = spaceColor;
    }

    /**
     * gets the color of space
     * @return color of space
     */
    public SPACECOLOR getSpacecolor() {
        return spacecolor;
    }

    /**
     * gets the index of space
     * @return index of space
     */
    public int getCellIdx() {
        return cellIdx;
    }

    /**
     * gets the piece on the space
     * @return piece on the space
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * if piece is not occupied and color of space is dark then return true as it is a valid move
     * @return true if valid move, else false
     */
    public boolean validMove(){
        Boolean flag = piece == null && spacecolor == SPACECOLOR.DARK;
        return flag;
    }
}