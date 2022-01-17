package com.webcheckers.model;

import java.util.Iterator;

public class Row implements Iterable<Space>{

    //attributes
    private Space[] spaces;
    private int index;

    /**
     * Constructor for row
     * @param index index of each space in the spaces[]
     * @param color color of each space
     */
    public Row(int index, Piece.pieceColor color) {
        this.index = index;
        this.spaces = new Space [8];

        //sets the type of pieces
        Piece.Type startingType = Piece.Type.SINGLE;
        //initialize opponent player's checker color reference
        Piece.pieceColor opponentPieceColor;
        //if current player's checker piece is white then assign opponent player's checker piece red otherwise white
        if (color == Piece.pieceColor.WHITE){
            opponentPieceColor = Piece.pieceColor.RED;
        }else{
            opponentPieceColor = Piece.pieceColor.WHITE;
        }

        //fill row with spaces and pieces
        //pieces should only be placed on dark spaces
        switch (index){
            case 0: //leftmost space is light and alternatively dark with current player pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK, null, new Piece(startingType, color));
                break;
            case 1: //leftmost space is dark and alternatively light with current player pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, new Piece(startingType, color),null);
                break;
            case 2: //leftmost space is light and alternatively dark with current player pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK, null, new Piece(startingType, color));
                break;
            case 3: //leftmost space is dark and alternatively light without any pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, null,null);
                break;
            case 4: //leftmost space is light and alternatively dark without any pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK, null,null);
                break;
            case 5: //leftmost space is dark and alternatively light with opponent player pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, new Piece(startingType, opponentPieceColor),null);
                break;
            case 6: //leftmost space is light and alternatively dark with opponent player pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK,null, new Piece(startingType, opponentPieceColor));
                break;
            case 7: //leftmost space is dark and alternatively light with opponent player pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, new Piece(startingType, opponentPieceColor),null);
                break;
        }
    }

    /**
     * initialize the spaces array for the given row
     * @param firstSpaceColor the color of the first space in the given row
     * @param secondSpaceColor the color of the second space in the given row
     * @param firstPiece the first piece in the given row
     * @param secondPiece the second piece in the given row
     * if even index then space color is firstSpaceColor else secondSpaceColor
     */
    private void handleSpaces(Space.SPACECOLOR firstSpaceColor, Space.SPACECOLOR secondSpaceColor, Piece firstPiece, Piece secondPiece) {
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) { // if even index then place first type of Color passed
                spaces[i] = new Space(i, firstSpaceColor, firstPiece);
            } else { //if odd index then place second type of Color passed
                spaces[i] = new Space(i, secondSpaceColor, secondPiece);
            }
        }
    }

    /**
     * @return return an iterator that iterates over each space in the row
     */
    public Iterator<Space> iterator() {
        return new SpaceIterator(spaces);
    }

    /**
     * @return return an iterator that iterates backwards over each space in the row
     */
    public Iterator<Space> reverseIterator(){return new ReverseSpaceIterator(spaces);}

    /**
     *  Iterates over an array of spaces.
     */
    public class SpaceIterator implements Iterator<Space> {
        private int index;
        private Space[] spaces;

        /**
         * A constructor for the iterator
         * @param spaces - the array of spaces in the row
         */
        public SpaceIterator(Space[] spaces) {
            this.index = 0;
            this.spaces = spaces;
        }


        /**
         * Checks if the row has another space to iterate over
         * @return true if there is another space left.
         */
        @Override
        public boolean hasNext() {
            return spaces.length >= index + 1;
        }

        /**
         * @return Returns the next space in the iterator
         */
        @Override
        public Space next() {
            Space nextSpace = spaces[index];
            index++;
            return nextSpace;
        }
    }

    /**
     * Checks how to iterate over an array of spaces in reverse order
     */
    public class ReverseSpaceIterator implements Iterator<Space> {
        //attributes
        private int index;
        private Space[] spaces;

        /**
         * A constructor for the reverse space iterator
         * @param spaces - the array of spaces in the row
         */
        public ReverseSpaceIterator(Space[] spaces) {
            this.index = 7;
            this.spaces = spaces;
        }


        /**
         * Checks if the row has another space to iterate over
         * Checks if index is greater or equal to 0
         * @return true if there is another space left.
         */
        @Override
        public boolean hasNext() {
            return index>=0;
        }

        /**
         * @return the next space in the iteration
         */
        @Override
        public Space next() {
            Space nextSpace = spaces[index];
            index--;
            return nextSpace;
        }
    }

    /**
     * gets a space at a given index
     * @param cell the index to get
     */
    public Space getSpace(int cell){
        return spaces[cell];
    }

    /**
     * returns the index of the row
     */
    public int getIndex() {
        return index;
    }

    /**
     * Updates the row by replacing new space when a piece is moved
     * @param cell the index to be replaced
     * @param space the space with which to replace it
     */
    public void replaceSpace(int cell, Space space){
        spaces[cell] = space;
    }

}
