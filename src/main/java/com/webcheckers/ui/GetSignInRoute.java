package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.*;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Sign In page.
 */
public class GetSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    //vm attributes as key/value name variables
    private static final Message WELCOME_MSG = Message.info("Welcome to the sign-in page");
    private final TemplateEngine templateEngine;
    static final String VIEW_NAME = "signin.ftl";
    static final String TITLE_KEY = "title";
    static final String TITLE = "Sign In";
    static final String MSG_KEY = "message";
    static final Message MSSG_VAL = Message.info("Enter your name to sign in");

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetHomeRoute is initialized.");
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
    public Object handle(Request request, Response response) {
        LOG.info("GetSignInRoute is invoked.");

//        get the current httpSession if there is one
        final Session httpSession = request.session();

        //if the player is signed in, show the list of signed in players and use home.ftl
        if (httpSession.attribute(PostSignInRoute.PLAYER_KEY) != null){
            response.redirect(WebServer.HOME_URL);
            // render the View
            return null;

        }else {
//        the player is not signed in, and must sign in.
            //initialize the VM
            Map<String, Object> vm = new HashMap<>();
            vm.put(TITLE_KEY, TITLE);
            // display a user message in the Home page
            vm.put(MSG_KEY, MSSG_VAL);

            // render the View
            return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
        }
    }
}
