package com.webcheckers.model;

import java.awt.*;

public class Piece {

    /**
     * Enum for color of pieces
     */
    public enum pieceColor{
        WHITE, RED
    }

    /**
     *  Enum for type of pieces
     */
    public enum Type {
        SINGLE, KING
    }

    //attributes
    private Type type;
    private pieceColor color;

    /**
     * Constructor for Piece
     * @param type
     * @param color
     */
    public Piece(Type type, pieceColor color){
        this.type = type;
        this.color = color;
    }

    /**
     * Gets type of Piece
     * @return type of Checker piece
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets color of Piece
     * @return color of piece red/white
     */
    public pieceColor getColor() {
        return color;
    }
}
