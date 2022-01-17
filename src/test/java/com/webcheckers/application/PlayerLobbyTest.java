package com.webcheckers.application;

import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import java.util.ArrayList;



class PlayerLobbyTest {
    private String playerName;
    private PlayerLobby CuT;
    private Player firstPlayer;
    private Player secondPlayer;



    @BeforeEach
    public void setup() {
        playerName = "Ruth";
        CuT = new PlayerLobby();
    }

    private Player newPlayerObject = new Player("Ruth");

    // testing if a player is removed
    @Test
    public void testingIfPlayerIsRemoved() {
        //creating testing environment
        PlayerLobby lobby = new PlayerLobby();
        // adding a player
        lobby.verifyAndAddNewPlayer(newPlayerObject);
        //removing a player
        lobby.removePlayer("Ruth");
        //assert
        assertEquals(lobby.getPlayer("Ruth"),null," player is removed");

    }

    //testing getPlayers
    @Test
    public void testingGetPlayers() {

        assertEquals("ArrayList", CuT.getPlayers().getClass().getSimpleName(),"fetched player list");

    }

    // testing is a player is unique method
    @Test
    public void testingIfUserIsUniqueName(){
        //creating player object
        Player player = new Player("@Ruth");
        assertTrue(CuT.isUniqueName(player.getName()),"the player is unique");
    }

    // testing getplayer method
    @Test
    public void testingGetPlayerMethod(){
        // adding a player to the lobby
        CuT.verifyAndAddNewPlayer( newPlayerObject);
        assertEquals("Ruth", CuT.getPlayer(playerName).getName(), " Player is got ");
    }

}
