package com.webcheckers.ui;
import java.util.*;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.application.*;
import com.webcheckers.model.*;
import com.webcheckers.util.MoveValidationHelper;
import spark.*;

import com.webcheckers.util.Message;

public class PostValidateMoveRoute implements Route{

    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final Gson gson;
    private final GameCenter gameCenter;

    //Constants
    static final String LAST_JUMP_MOVE_INFO = "The move is the last continuation of a jump.";
    static final String CONTINUE_JUMP_MOVE_INFO = "The move is valid continuation of a jump.";
    static final String CONTINUE_JUMP_MOVE_ERROR = "You must continue the jump.";
    static final String START_MULTIPLE_JUMP_MOVE_INFO = "The move is a start to a multiple jump.";
    static final String VALID_SINGLE_JUMP_INFO = "The move is a valid single jump.";
    static final String VALID_JUMP_MOVE_ERROR = "You must take a valid jump.";
    static final String VALID_FORWARD_MOVE_INFO = "The move is a valid forward move.";
    static final String VALID_FORWARD_MOVE_ERROR = "The move is not a valid forward move.";


    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostValidateMoveRoute(final TemplateEngine templateEngine, Gson gson, GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(gameCenter, "Game center must not be null");
        //
        this.templateEngine = templateEngine;
        this.gson = gson;
        this.gameCenter = gameCenter;

        LOG.config("PostValidateMoveRoute is initialized.");
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

        ArrayList<Move> possibleMoves = new ArrayList<>();
        ArrayList<ArrayList<Move>> possibleJumps = new ArrayList<>();


        //Get the move made from the session attributes
        Move move_made = gson.fromJson(request.queryParams("actionData"), Move.class);
        System.out.println(move_made);

        //get the player that made the move from the session attributes
        Player thisPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);
        Game game = null;

        // fetches the game where this player is in progress
        if (thisPlayer != null){
            game = gameCenter.getGame(thisPlayer.getName());
        }

        // if game is in progress and a move is already made
        if (move_made != null && game != null) {
            BoardView board = game.getBoard();
            Piece.pieceColor activeColor = game.getActiveColor();

            if (game.getMoveSequence() != null){
                if (move_made.equals(game.getLastMoveToMake())){
                    game.incrementMovesMade();
                    return gson.toJson(Message.info(LAST_JUMP_MOVE_INFO));
                }
                else if (move_made.equals(game.getNextMoveToMake())){
                    game.incrementMovesMade();
                    return gson.toJson(Message.info(CONTINUE_JUMP_MOVE_INFO));
                }
                return gson.toJson(Message.error(CONTINUE_JUMP_MOVE_ERROR));
            }

            //scan the board for all available moves
            new MoveValidationHelper().scanBoard(board,activeColor,possibleMoves, possibleJumps);

            //checks if there are any jumps
            if(possibleJumps.size() > 0){
                for(ArrayList<Move> sequence : possibleJumps){
                    if(move_made.equals(sequence.get(0))){
                        if (sequence.size() > 1){
                            game.setMoveSequence(sequence);
                            game.incrementMovesMade();
                            return gson.toJson(Message.info(START_MULTIPLE_JUMP_MOVE_INFO));
                        }else if (sequence.size() == 1){
                            game.setMoveSequence(sequence);
                            game.incrementMovesMade();
                            return gson.toJson(Message.info(VALID_SINGLE_JUMP_INFO));
                        }
                    }
                }
                return gson.toJson(Message.error(VALID_JUMP_MOVE_ERROR));
            }

            //if there are no jumps
            //check if the move made exists in single moves
            else if(possibleMoves.size() > 0){
                for(Move temp_move: possibleMoves){
                    if(move_made.equals(temp_move)){
                        ArrayList<Move> singleSequence = new ArrayList<>();
                        singleSequence.add(move_made);
                        game.setMoveSequence(singleSequence);
                        game.incrementMovesMade();
                        return gson.toJson(Message.info(VALID_FORWARD_MOVE_INFO));
                    }
                }
                return  gson.toJson(Message.error(VALID_FORWARD_MOVE_ERROR));
            }
        }
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}

