package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * A unit test for the model Player component
 */
@Tag("Model-tier")
public class PlayerTest {

    //private static final attributes
    private String PlayerName1 = "Rhino";
    private String PlayerName2 = "Donkey";

    //test getName
    @Test
    public void getName(){
        Player CuT = new Player(PlayerName1);
        assertEquals(PlayerName1, CuT.getName(), "Name not equal to " + PlayerName1);
    }
}
