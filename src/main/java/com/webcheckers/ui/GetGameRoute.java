package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.*;
import com.webcheckers.util.Message;
import com.webcheckers.model.*;
import com.webcheckers.util.MoveValidationHelper;
import spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.*;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * a route to render the game page
 * invoked by GET /game
 */
public class GetGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    //attribute keys for the vm
    static final String TITLE_KEY = "title";
    static final String TITLE = "Home";
    static final String VIEW_MODE_KEY = "viewMode";
    static final String CURR_PLAYER_KEY = "currentUser";
    static final String MESSAGE_KEY = "message";
    static final String RED_PLAYER_KEY = "redPlayer";
    static final String WHITE_PLAYER_KEY = "whitePlayer";
    static final String ACTIVE_COLOR_KEY = "activeColor";
    static final String BOARD_KEY = "board";
    static final String FLIPPED_KEY = "flipped";
    static final Message GAME_MESSAGE_VAL = Message.info("Game On");
    static final Message ERROR_MESSAGE_VAL = Message.info("Oops the player you selected is already in game. Try another player!");
    static final String GAME_FTL = "game.ftl";
    static final String MODE_OPTIONS_KEY = "modeOptionsAsJSON";

    private String viewMode, redirect;

    protected enum gameMode {
        PLAY,
        SPECTATOR
    }

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameCenter gameCenter;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetGameRoute(TemplateEngine templateEngine, PlayerLobby playerLobby, GameCenter gameCenter, Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null.");
        Objects.requireNonNull(playerLobby, "lobby must not be null.");
        Objects.requireNonNull(playerLobby, "gameCenter must not be null.");
        Objects.requireNonNull(gson, "gson must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameCenter = gameCenter;
        this.gson = gson;

    }

    /**
     * Render the WebCheckers board.
     *
     * @param request  the HTTP request.
     * @param response the HTTP response.
     * @return the rendered HTML for the board.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetGameRoute is invoked.");
        //retrive the http session
        final Session httpSession = request.session();
        //initialize the vm
        Map<String, Object> vm = new HashMap<>();
        Player secondPlayer;
        Player currentPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);

        if (currentPlayer != null) {

            if(gameCenter.isPlayerASpectator(currentPlayer)) {
                viewMode = gameMode.SPECTATOR.toString();
                redirect = WebServer.ENDSPECTATE_URL;
                Game game = gameCenter.getSpectatorGame(currentPlayer);
                if(game == null || game.gameOver()) {
                    response.redirect(WebServer.HOME_URL);
                }
                LOG.fine(String.format("Rendering a Spectator for: %s",game.toString()));

                //add attributes to the map
                vm.put(RED_PLAYER_KEY, playerLobby.getPlayer(game.getRedPlayerName()));
                vm.put(WHITE_PLAYER_KEY, playerLobby.getPlayer(game.getWhitePlayerName()));
                vm.put(ACTIVE_COLOR_KEY, game.getActiveColor());
                vm.put(BOARD_KEY, game.getBoard());
                vm.put("viewMode", viewMode);
                vm.put(TITLE_KEY, "Spectate");
                vm.put(CURR_PLAYER_KEY, currentPlayer);
                vm.put(FLIPPED_KEY, false);

                return templateEngine.render(new ModelAndView(vm, GAME_FTL));
            }

            String secondPlayerName = request.queryParams("name");
            secondPlayer = playerLobby.getPlayer(secondPlayerName);

            vm.put(TITLE_KEY, "New Game");
            vm.put(CURR_PLAYER_KEY, currentPlayer);
            vm.put(VIEW_MODE_KEY, gameMode.PLAY);
            vm.put(MESSAGE_KEY, GAME_MESSAGE_VAL);

            if (gameCenter.getGame(currentPlayer.getName()) == null && gameCenter.getGame(secondPlayerName) != null) {

                return error(response, currentPlayer);
            }
            else if (gameCenter.getGame(currentPlayer.getName()) == null) {

                Game game = new Game(new BoardView(), currentPlayer.getName(), secondPlayer.getName(), Piece.pieceColor.RED, new MoveValidationHelper());
                gameCenter.addGame(currentPlayer.getName(), game);
                gameCenter.addGame(secondPlayerName, game);

                //add attributes to the map
                vm.put(RED_PLAYER_KEY, playerLobby.getPlayer(game.getRedPlayerName()));
                vm.put(WHITE_PLAYER_KEY, playerLobby.getPlayer(game.getWhitePlayerName()));
                vm.put(ACTIVE_COLOR_KEY, game.getActiveColor());
                vm.put(BOARD_KEY, game.getBoard());

                //the red player clicks first, so board remains unflipped
                vm.put(FLIPPED_KEY, false);

                return templateEngine.render(new ModelAndView(vm, GAME_FTL));
            }
            else if(gameCenter.getGame(currentPlayer.getName()) != null) {
                Game game = gameCenter.getGame(currentPlayer.getName());

                //add attributes to the map
                vm.put(RED_PLAYER_KEY, playerLobby.getPlayer(game.getRedPlayerName()));
                vm.put(WHITE_PLAYER_KEY, playerLobby.getPlayer(game.getWhitePlayerName()));
                vm.put(ACTIVE_COLOR_KEY, game.getActiveColor());
                vm.put(BOARD_KEY, game.getBoard());

                //the red player clicks first, so board remains unflipped
                if(game.getRedPlayerName().equalsIgnoreCase(currentPlayer.getName())) {
                    vm.put(FLIPPED_KEY, false);
                }
                else {
                    vm.put(FLIPPED_KEY, true);
                }

                //check if the game is over for any reason
                if (game.gameOver()) {
                    final Map<String, Object> modeOptions = new HashMap<>(2);
                    modeOptions.put("isGameOver", true);
                    String gameEndMsg = game.getGameEndMsg();
                    String winner = game.getWinner();
                    if (winner.equals(currentPlayer.getName())) {
                        gameEndMsg += " You won the game!";
                    } else {
                        gameEndMsg += " " + winner + " won the game.";
                    }
                    modeOptions.put("gameOverMessage", gameEndMsg);
                    vm.put(MODE_OPTIONS_KEY, gson.toJson(modeOptions));
                    gameCenter.removeGameBoard(currentPlayer.getName());
                }

                //add help section if this is the active player
                //in else block because there is no point in giving help if game is over
                else if (game.amIActive(currentPlayer)) {
                    vm.put("piecesMSG",game.generatePiecesMsg());
                    vm.put("moves",game.generateMovesList());
                }

                return templateEngine.render(new ModelAndView(vm, GAME_FTL));
            }else{
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
        }
        response.redirect(WebServer.HOME_URL);
        //halt();
        return null;
    }

    /**
     * if the selected player is already in game, then current player is kept in the player lobby with error message displayed
     * @param player Current player who selects the second player
     * @return the rendered HTML for the homepage with error message.
     */
    private String error(Response response, Player player) {
        LOG.info("GetGameRoute-Error is invoked");

        //add each online player
        List<Player> players = playerLobby.getPlayers();
        //make a copy of the players list, remove the current player
        //so that the current player does not show up as an available player
        List<Player> copy = new ArrayList<Player>(players);
        copy.remove(player);

        Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE_KEY, "Home");

        // display a user message in the Home page
        vm.put(GetHomeRoute.MESSAGE_KEY, ERROR_MESSAGE_VAL);
        vm.put(GetHomeRoute.PLAYERS_LIST_KEY, copy);

        //add the current player
        vm.put(GetHomeRoute.CURRENT_PLAYER_KEY, player);

        if (gameCenter.getGame(player.getName()) != null) {
            response.redirect(WebServer.GAME_URL);
        }

        // render the View
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
    }
}