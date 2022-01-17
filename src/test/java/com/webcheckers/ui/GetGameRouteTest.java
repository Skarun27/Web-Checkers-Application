package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.ui.GetGameRoute;
import com.webcheckers.ui.GetHomeRoute;
import com.webcheckers.ui.PostSignInRoute;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Message;
import com.webcheckers.util.MoveValidationHelper;
import org.junit.Assert;
import static org.junit.jupiter.api.Assertions.*;
import spark.*;

import java.util.*;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
@Tag ("UI-Tier")
/**
 * a route to render the game page
 * invoked by GET /game
 */
public class GetGameRouteTest  {

    private Request request;
    private Response response;
    private Session session;
    private GetGameRoute gameRoute;

    private TemplateEngine templateEngine;
    private PlayerLobby playerLobby;
    private GameCenter gameCenter;
    private Gson gson;

    private Player currentPlayer;
    private Player secondPlayer;
    private Piece.pieceColor activeColor;
    private BoardView board;
    private MoveValidationHelper helper;
    private Game game;

    @BeforeEach
    public void testBuild(){
        request=mock(Request.class);
        response=mock(Response.class);
        session=mock(Session.class);

        when(request.session()).thenReturn(session);

        templateEngine=mock(TemplateEngine.class);
        playerLobby=mock(PlayerLobby.class);
        gameCenter=mock(GameCenter.class);
        gson=new Gson();
        helper=mock(MoveValidationHelper.class);
        gameRoute= new GetGameRoute(templateEngine,playerLobby,gameCenter, gson);
    }

    @Test
    public void newGame() throws Exception {
        currentPlayer=new Player("red");
        secondPlayer=new Player("white");
        playerLobby.verifyAndAddNewPlayer(secondPlayer);
        when(request.session()).thenReturn(session);
        when(session.attribute(PostSignInRoute.PLAYER_KEY)).thenReturn(currentPlayer);
        when(playerLobby.getPlayer(null)).thenReturn(secondPlayer);
        gameRoute.handle(request,response);

    }
    @Test
    public void whiteGame() throws Exception {
        currentPlayer=new Player("red");
        secondPlayer=new Player("white");
        activeColor= Piece.pieceColor.RED;
        board=mock(BoardView.class);
        when(session.attribute("Player")).thenReturn(secondPlayer);
        when(playerLobby.getPlayer("white")).thenReturn(secondPlayer);
        gameRoute.handle(request,response);

    }
    @Test
    public void currentInGame() throws Exception {
        currentPlayer=new Player("red");
        secondPlayer=new Player("white");
        Player otherPlayer=new Player("other");
        playerLobby.verifyAndAddNewPlayer(secondPlayer);
        game=new Game(board,currentPlayer.getName(),otherPlayer.getName(),activeColor,helper);
        when(request.session()).thenReturn(session);
        when(session.attribute(PostSignInRoute.PLAYER_KEY)).thenReturn(currentPlayer);
        when(playerLobby.getPlayer(null)).thenReturn(secondPlayer);
        when(gameCenter.getGame(currentPlayer.getName())).thenReturn(game);
        gameRoute.handle(request,response);
    }

   @Test
    public void gameOverTest() throws Exception{
       currentPlayer=new Player("red");
       secondPlayer=new Player("white");
       playerLobby.verifyAndAddNewPlayer(secondPlayer);
       game=mock(Game.class);
       when(request.session()).thenReturn(session);
       when(session.attribute(PostSignInRoute.PLAYER_KEY)).thenReturn(currentPlayer);
       when(playerLobby.getPlayer(null)).thenReturn(secondPlayer);
       when(gameCenter.getGame(currentPlayer.getName())).thenReturn(game);
       when(game.getRedPlayerName()).thenReturn(currentPlayer.getName());
       when(game.getWinner()).thenReturn(currentPlayer.getName());
       when(game.gameOver()).thenReturn(true);
       gameRoute.handle(request,response);
       when(game.getWinner()).thenReturn(secondPlayer.getName());
       gameRoute.handle(request,response);
    }

    @Test
    public void noPlayer() throws Exception{
        when(request.session().attribute(PostSignInRoute.PLAYER_KEY)).thenReturn(null);

        try {
            gameRoute.handle(request, response);
        }catch(Exception e){e.printStackTrace();}
        verify(response).redirect(WebServer.HOME_URL);


    }

    @Test
    public void noOpponent() throws Exception{
        currentPlayer=new Player("red");
        secondPlayer=new Player("white");
        playerLobby.verifyAndAddNewPlayer(secondPlayer);
        Player otherPlayer=new Player("other");
        playerLobby.verifyAndAddNewPlayer(secondPlayer);
        game=new Game(board,secondPlayer.getName(),otherPlayer.getName(),activeColor,helper);
        gameCenter.addGame(secondPlayer.getName(),game);
        when(request.session()).thenReturn(session);
        when(session.attribute(PostSignInRoute.PLAYER_KEY)).thenReturn(currentPlayer);
        when(playerLobby.getPlayer(null)).thenReturn(secondPlayer);
        when(gameCenter.getGame(currentPlayer.getName())).thenReturn(null);
        when(gameCenter.getGame(secondPlayer.getName())).thenReturn(game);
        gameRoute.handle(request,response);
    }

}
