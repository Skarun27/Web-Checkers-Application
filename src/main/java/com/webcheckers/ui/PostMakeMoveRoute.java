package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostMakeMoveRoute implements Route {

    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final TemplateEngine templateEngine;
    private final Gson gson;
    private final PlayerLobby lobby;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */

    public PostMakeMoveRoute(final TemplateEngine templateEngine, Gson gson, PlayerLobby lobby, GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(lobby, "lobby must not be null");
        Objects.requireNonNull(gameCenter, "Game center must not be null");
        //
        this.templateEngine = templateEngine;
        this.gson = gson;
        this.lobby = lobby;
        this.gameCenter = gameCenter;

        LOG.config("PostMakeMoveRoute is initialized.");
    }



    /**
     * Render the WebCheckers Game page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the validate move page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.info("PostValidateMoveRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();

        //get the player that made the move from the session attributes
        Player thisPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);
        Game game = null;

        //if this player is signed in, fetch the game this player is in
        if (thisPlayer != null){
            game = gameCenter.getGame(thisPlayer.getName());
        }

        //if this player is signed in and there is a board
        if (game != null) {

            //if there is a game for this player
            if(game.getMoveSequence() != null) {
                if(game.allMovesMade()) {
                    game.MakeMoves();
                    return gson.toJson(Message.info("Valid move was made."));
                }
                return gson.toJson(Message.error("Multi jump is pending"));
            }else {
                game.MakeMoves();
                return gson.toJson(Message.info("Valid move was made."));
            }
        }
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
