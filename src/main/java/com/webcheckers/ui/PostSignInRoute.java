package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.util.Message;

public class PostSignInRoute implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    //vm attributes as key/value name variables
    static final String TITLE_KEY = "title";
    static final String TITLE = "Sign In";
    static final String MESSAGE_KEY = "message";
    static final String PLAYER_KEY = "currentPlayer";
    static final Message ERROR_MSG = Message.info("Either a user with same name is already there in lobby, or the username does not include any alphanumeric character!");

    //objects initialized of different types
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /guess} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} for the application to use when rendering HTML responses.
     *
     * @param playerLobby
     * @throws NullPointerException
     *    when the {@code gameCenter} or {@code templateEngine} parameter is null
     */
    PostSignInRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby) {
        this.playerLobby = playerLobby;
        // validation of template Engine object
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
    }

    /**
     * Render the WebCheckers Sign-In page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public String handle(Request request, Response response) {
        //render the page
        LOG.info("PostSignInRoute is invoked.");

        // retrieve the HTTP session
        final Session httpSession = request.session();

        //retrieve the username parameter
        final String username = request.queryParams("PlayerName");
        final Player thisPlayer = new Player(username);
        httpSession.attribute(PLAYER_KEY, thisPlayer);

        //attempt to add the player to the lobby
        //'added' will tell whether it was successful or not
        boolean added = playerLobby.verifyAndAddNewPlayer(thisPlayer);

        if (added){
            //handle success

            /**
             * The logic below can be simplified by redirecting the user to the home page
             * The redirect essentially just calls get home route and uses the logic from that page'
             * The goal of the redirect is to prevent limit the same code existing in multiple routes
             *
             * The error handling is still useful though.
             */

            response.redirect(WebServer.HOME_URL);
            return null; //the thought is that the program will never get here because of the redirect
        }else{
            //handle an error - go back to sign in page
            Map<String, Object> vm = new HashMap<>();
            vm.put("title", "Sign In");
            vm.put("message", ERROR_MSG);
            // render the View
            return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
        }
    }
}
