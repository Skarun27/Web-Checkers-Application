package com.webcheckers.application;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

public class GameCenter {

    //declaring the hashmap with key as name of player and value as game object where the player is playing
    private HashMap<String, Game> gameMap;
    private HashMap<Player, Player> spectators;
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

    /**
     * Constructor for Game center
     */
    public GameCenter() {
        gameMap = new HashMap<>();
        spectators = new HashMap<>();
    }

    /**
     * Add games to the game map with player names as key to the game value in hashmap
     * @param name the name of player playing the game
     * @param game the game being played by the mentioned player
     */
    public void addGame(String name, Game game) {
        gameMap.put(name, game);
        LOG.config(String.format("%s added to a game.", name));
    }

    /**
     * get a game from the game Map that corresponds to the player name
     * @param name the name of the player playing that game
     * @return Game object of which the player is a part of
     */
    public Game getGame(String name) {
        return gameMap.get(name);
    }

    /**
     * remove a name,game pair from the map
     * @param name the name of the player that is the key to be removed
     */
    public void removeGameBoard(String name){
        gameMap.remove(name, getGame(name));
        LOG.config(String.format("%s removed from a game.", name));
    }

    /**
     * Checks and returns true if a player is a spectator
     * @param player player to be checked whether spectator or not
     * @return true if the player is a spectator
     */
    public boolean isPlayerASpectator(Player player){
        return spectators.containsKey(player);
    }

    /**
     * Adds a spectator to the game
     * @param spectator Player added as a spectator
     * @param player player to spectate
     */
    public void addSpectator(Player spectator, Player player){
        LOG.fine(String.format("%s is spectating %s's game", spectator.getName(), player.getName()));
        spectators.put(spectator, player);
    }

    /**
     * Determines whether a given player is in-game
     *
     * @param player - player that the method checks if in game
     * @return - true if player is in-game, else false
     */
    public boolean isPlayerInAGame(Player player) {

        if (gameMap == null || player == null) return false;

        if(gameMap.containsKey(player.getName())) {
            Game game = gameMap.get(player.getName());
            if(player.getName().equals(game.getRedPlayerName()))
                return true;
        }
        return false;
    }

    /**
     * Gives all active games
     * @return Map of all active Games
     */
    public HashMap<String, Game> getGameList(){
        HashMap<String, Game> games = new HashMap<>();
        for (Map.Entry<String, Game> entry : gameMap.entrySet()) {
            games.put(entry.getValue().toString(), entry.getValue());
        }
        return games;
    }

    /**
     * Gets the game spectated by the given player
     * @param player spectator
     * @return the game which is spectated
     */
    public Game getSpectatorGame(Player player){
        return this.getGame(spectators.get(player).getName());
    }

    /**
     * Removes player from Spectator map
     * @param player player to be removed
     */
    public void removeSpectator(Player player){
        spectators.remove(player);
    }
}
