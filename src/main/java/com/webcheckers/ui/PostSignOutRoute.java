package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Sign Out Route for the Checker Game
 */
public class PostSignOutRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignOutRoute.class.getName());
    private TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final  PlayerLobby playerLobby;

    PostSignOutRoute(final TemplateEngine templateEngine, final GameCenter gameCenter, PlayerLobby playerLobby) {
        Objects.requireNonNull(templateEngine, "templateEngine should not be null.");
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
        this.playerLobby=playerLobby;

    }


    @Override
    public Object handle(Request request, Response response) throws Exception {

        //get the username
        Session httpSession = request.session();
        Player currentPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);
        String username= currentPlayer.getName();
        playerLobby.removePlayer(username);
        // remove from the session
        httpSession.removeAttribute(PostSignInRoute.PLAYER_KEY);

        response.redirect(WebServer.HOME_URL);
        return response;



    }

}
