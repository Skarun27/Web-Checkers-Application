package com.webcheckers.application;

import com.webcheckers.model.Player;
import com.webcheckers.ui.GetSignInRoute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PlayerLobby {

    //stores all the player objects created in an Arraylist
    private static ArrayList<Player> playersList = new ArrayList<>();

    //stores all the player objects created in key/value pairs
    private static HashMap<String, Player> playersMap = new HashMap<>();

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());
    private static Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");

    /**
     * Checks if the given string input contains alphanumeric characters or not
     * @param username Username of the player signing in to the game
     * @return true if the username contains alphanumeric chars, otherwise false
     */
    public static boolean isAlphaNumeric(String username) {
        return username.matches("[\\w ]*[\\w]+[\\w ]*");
    }

    /**
     * Checks if the given username is unique among the players present in the lobby
     * @param username name of the user to be verified
     * @return true if the player is unique otherwise false
     */
    public static boolean isUniqueName(String username) {

            for (Player loggedInPlayer : playersList) {

                //check the current player username with already existing player's username
                if (loggedInPlayer.getName().equals(username)) {
                    return false;
                }
            }
        return true;
    }

    /**
     * Checks if the username entered in the sign-in page is valid or not
     *
     * Two validity checks are done:
     * 1. Username should contain atleast 1 alphanumeric character
     * 2. Username should be unique among all the players present in the lobby currently
     *
     * @param newPlayerObject User input on the signin page for the player name
     * @return true if both the above conditions satisfy otherwise false
     */
    public boolean verifyAndAddNewPlayer(Player newPlayerObject) {
        //checks if the username contains atleast 1 alphanumeric character
        boolean isAlphanumeric = isAlphaNumeric(newPlayerObject.getName());

        //checks if there is no user with the same name already present in the lobby
        boolean isNameUnique = isUniqueName(newPlayerObject.getName());

        //if both the conditions are true, add the current player object to the players list and players map
        if (!isAlphanumeric || !isNameUnique) return false; //player not added to list
        playersList.add(newPlayerObject);
        playersMap.put(newPlayerObject.getName(), newPlayerObject);

        LOG.config(String.format("Player %s added to the lobby",newPlayerObject.getName()));
        return true;
    }

    /**
     * Fetches the players list to display on the home page
     * @return players
     */
    public ArrayList<Player> getPlayers() {
        return playersList;
    }

    /**
     * Fetches the player object based on the name passed
     * @playerName name of the player
     * @return player object
     */
    public Player getPlayer(String playerName) {
        return playersMap.get(playerName);
    }

    /**
     * Removes the player from the online players list
     * @param username name of the player
     */
    public void removePlayer(String username){
        // remove from hashmap
        playersMap.remove(username);
        //remove from list
        for(int i=0; i < playersList.size(); i++){
            if (playersList.get(i).getName().equals(username)){
                playersList.remove(i);
                break;
            }
        }
    }

    /**
     * This may be an interim method.
     * @return - Hashmap containing all the active players
     */
    public ArrayList<Player> getActivePlayers() {
        return playersList;
    }

}
