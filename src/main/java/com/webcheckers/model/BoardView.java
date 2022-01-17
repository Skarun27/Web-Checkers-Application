package com.webcheckers.model;

import java.util.Iterator;

/**
 * Stores all the rows and column positions on the board
 * Implementing two dimensional arrays
 */
public class BoardView implements Iterable<Row>{
    //attribute
    private Row[] rows;

    /**
     * constructor for board view
     */
    public BoardView(){
        Piece.pieceColor color = Piece.pieceColor.WHITE;
        rows = new Row[8];
        for(int i=0; i<8; i++){
            rows[i] = new Row(i, color);
        }
    }

    /**
     * update the board with a move
     * if there is a jump, remove the jumped piece
     * @param move the move that is being made
     * @param activeColor
     */
    public void makeMove(Move move, Piece.pieceColor activeColor){
        Position start = move.getStart(); // return start of move and update
        Position end = move.getEnd(); // return end of move and update
        Space startSpace = getSpace(start.getRow(), start.getCell());
        Piece startPiece = startSpace.getPiece();

        //set the start space to an empty square
        rows[start.getRow()].replaceSpace(start.getCell(), new Space(start.getCell(), Space.SPACECOLOR.DARK, null));

        //if the end piece is a single piece and is becomming a king
        if (startPiece.getType() == Piece.Type.SINGLE && (end.getRow() == 0 || end.getRow() == 7)){
            rows[end.getRow()].replaceSpace(end.getCell(), new Space(end.getCell(), Space.SPACECOLOR.DARK, new Piece(Piece.Type.KING, activeColor)));
        }else if (startPiece.getType() == Piece.Type.KING){
            //if a piece is already a king piece
            rows[end.getRow()].replaceSpace(end.getCell(), new Space(end.getCell(), Space.SPACECOLOR.DARK, new Piece(Piece.Type.KING, activeColor)));
        }else{
            //the piece stays a single
            rows[end.getRow()].replaceSpace(end.getCell(), new Space(end.getCell(), Space.SPACECOLOR.DARK, new Piece(Piece.Type.SINGLE, activeColor)));
        }

        //add the logic to remove the piece in the middle
        if (Math.abs(start.getRow() - end.getRow()) == 2){ // if the diff is  2 _> absolute value
            int centerRow = (start.getRow() + end.getRow())/2; // calculating the center row
            int centerCol = (start.getCell() + end.getCell())/2; // calculating the center column
            rows[centerRow].replaceSpace(centerCol, new Space(centerCol, Space.SPACECOLOR.DARK, null)); // updating
        }
    }

    /**
     * @return an iterator that iterates over each row on the board
     */
    @Override
    public Iterator<Row> iterator(){
        return new RowIterator(rows);
    }

    /**
     * Iterates over an array of rows
     */
    public class RowIterator implements Iterator<Row> {

        private int index;
        private Row[] rows;

        /**
         * A constructor for the iterator
         * @param rows - the array of rows on the board
         */
        public RowIterator(Row[] rows) {
            this.index = 0;
            this.rows = rows;
        }

        /**
         * Checks if the board has another row to iterate over
         * @return true if there is another row left.
         */
        @Override
        public boolean hasNext() {
            return rows.length >= index + 1;
        }

        /**
         * @return the next row in the iteration
         */
        @Override
        public Row next() {
            Row nextRow = rows[index];
            index++;
            return nextRow;
        }
    }

    /**
     * @return an iterator that iterates backwards over each row on the board
     */
    public Iterator<Row> reverseIterator(){
        return new ReverseRowIterator(rows); // returning the row iterator
    }

    /**
     * A class to define how to iterate over an array of spaces.
     */
    public class ReverseRowIterator implements Iterator<Row>{
        // class implementing the interface -> Iterator

        private  int index;
        private Row[] rows;

        /**
         * A constructor for the iterator
         * @param rows - the array of rows on the board
         */
        public ReverseRowIterator(Row[] rows){
            this.index = 7;
            this.rows = rows;
        }

        /**
         * Determines if the board has another row to iterate over
         * @return true if there is another row left.
         */
        @Override
        public boolean hasNext(){
            return this.index >= 0;
        }

        /**
         * @return the next row in the iteration
         * increments the index
         */
        @Override
        public Row next(){
            Row nextRow = rows[index];
            index--;
            return nextRow;
        }
    }

    /**
     * get a space at a given row, col coordinates
     */
    public Space getSpace(int row, int col){
        // returns Space type
        if (0 <= row && row <= 7 && 0<=col &&col<=7){
            return rows[row].getSpace(col);
        }
        return null;
    }
}
