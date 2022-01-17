package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;
import java.util.logging.Logger;

public class GetSpectatorViewRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostRevokeMoveRoute.class.getName());
    private final GameCenter gameCenter;
    private final PlayerLobby playerLobby;

    /**
     * Initializes the GetSignOutRoute
     *
     * @param playerLobby
     * @param gameCenter - game manager used to access games
     */
    GetSpectatorViewRoute(final PlayerLobby playerLobby, final GameCenter gameCenter) {
        Objects.requireNonNull(gameCenter, "Game Manager must not be null");
        Objects.requireNonNull(playerLobby, "Player Lobby must not be null");

        this.gameCenter = gameCenter;
        this.playerLobby = playerLobby;
    }

    /**
     * Removes the player from the player lobby and destroys the game
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - null
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.fine("GetSpectatorRoute invoked");
        Player spectator = request.session().attribute(PostSignInRoute.PLAYER_KEY);
        Player gamePlayer = playerLobby.getPlayer(request.queryParams("redPlayer"));

        LOG.fine("Adding Spectator");
        gameCenter.addSpectator(spectator, gamePlayer);

        LOG.fine("Redirecting to game");
        response.redirect(WebServer.GAME_URL);

        return null;
    }
}
