package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;
import java.util.logging.Logger;

public class PostEndSpectateRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostRevokeMoveRoute.class.getName());
    private final GameCenter gameCenter;

    /**
     * Initializes the GetSignOutRoute
     *
     * @param gameCenter - game manager used to access games
     */
    PostEndSpectateRoute(final GameCenter gameCenter) {
        Objects.requireNonNull(gameCenter, "Game Manager must not be null");

        this.gameCenter = gameCenter;
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
        LOG.fine("PostEndSpectateRoute invoked");
        Player spectator = request.session().attribute(PostSignInRoute.PLAYER_KEY);

        gameCenter.removeSpectator(spectator);
        request.session().attribute("message", Message.info("Spectating mode has ended."));
        response.redirect(WebServer.HOME_URL);

        return null;
    }
}
