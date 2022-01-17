package com.webcheckers.model;

public class Player {

    //attributes
    private final String name;

    /**
     * constructor for initializing Player
     * @param name - name of player
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Gets the name of player
     * @return - name of player
     */
    public String getName() {
        return name;
    }
}
