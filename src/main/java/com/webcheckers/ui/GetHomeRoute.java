package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  //vm attributes as key/value name variables
  static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  static final String TITLE = "Welcome!";
  static final String TITLE_KEY = "title";
  static final String MESSAGE_KEY = "message";
  static final String PLAYERS_LIST_KEY = "players";
  static final String NUMBER_PLAYERS_KEY = "playersSignedIn";
  static final String NUMBER_PLAYERS_MSG = "There are %d player(s) currently signed in.";
  static final String VIEW_NAME = "home.ftl";
  static final String CURRENT_PLAYER_KEY = "currentUser";
  static final String AVAILABLE_PLAYER_KEY = "availablePlayers";
  static final String SPECTATOR_VIEW = "spectatorRoute";
  public static final String UPDATED_STATUS_MSG = "updatedMsg";

  //objects initialized of different types
  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;
  private final GameCenter gameCenter;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *  @param templateEngine
   *   the HTML template rendering engine
   * @param playerLobby
   * @param gameCenter
   */
  public GetHomeRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby, GameCenter gameCenter) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.playerLobby = playerLobby;
    this.gameCenter = gameCenter;
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
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
    LOG.finer("GetHomeRoute is invoked.");

    //creates the hashmap
    Map<String, Object> vm = new HashMap<>();

    //fetches the current user session and fetches the current player object of type player
    final Session httpSession = request.session();
    Player currentPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);


    vm.put(TITLE_KEY, TITLE);
    //display a user message in the Home page
    vm.put(MESSAGE_KEY, WELCOME_MSG);

    //when the player is signed in, show the list of other players already in lobby
    if (currentPlayer != null){
      //add the current player to the hashmap
      vm.put(PostSignInRoute.PLAYER_KEY, currentPlayer);

      //store the list of players in lobby to display on the homepage
      ArrayList<Player> playersInLobby = playerLobby.getPlayers();

      //Store in a new arraylist so that current player can be removed from the current list of players to be displayed
      ArrayList<Player> onlinePlayers = new ArrayList<>(playersInLobby);

      //removes the current player object from the logged in players list and stores the hashmap
      onlinePlayers.remove(currentPlayer);
      vm.put(PLAYERS_LIST_KEY, onlinePlayers);
      vm.put("currentPlayer", currentPlayer);
      vm.put("activePlayers", playerLobby.getActivePlayers());
      vm.put("gameRoute", WebServer.GAME_URL);
      vm.put("hasGames", gameCenter.getGameList().size() > 0);
      vm.put("activeGames", gameCenter.getGameList());
      vm.put(SPECTATOR_VIEW, WebServer.SPECTATE_URL);

      //check if this player is in a game
      if (gameCenter.getGame(currentPlayer.getName()) != null) {
        response.redirect(WebServer.GAME_URL);
      }

      // render the View
      return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
    }

    //if the player is not signed in, only show the number of signed in players
    else{

      //get the online players list from playerlobby class
      ArrayList<Player> playersInLobby = playerLobby.getPlayers();

      //if there is atleast 1 player in the lobby already capture the count of numbers of players online in hashmap
      if(playersInLobby != null) {
        int playersSize = playersInLobby.size();
        boolean availablePlayers = playersSize > 0;
        vm.put(AVAILABLE_PLAYER_KEY, availablePlayers);
        vm.put(NUMBER_PLAYERS_KEY, String.format(NUMBER_PLAYERS_MSG, playersSize));
      }

      //if the players list is empty
      else {
        vm.put(NUMBER_PLAYERS_KEY, String.format(NUMBER_PLAYERS_MSG, 0));
      }

      // render the View
      return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
    }
  }
}
