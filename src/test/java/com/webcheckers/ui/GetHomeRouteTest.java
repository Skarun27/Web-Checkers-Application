package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;


import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import spark.*;
/**
 *
 * @author rk4850@rit.edu
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetHomeRouteTest {
    // mock objects
    private GetHomeRoute home;
    private Request request;
    private TemplateEngine templateEngine;
    private Response response;
    private GameCenter gameCenter;
    private PlayerLobby playerLobby;
    private Session HttpSession;
    private Player player;
    private  ArrayList arrayList;
    private  TemplateEngine engine;


    @BeforeEach
    public void setup(){
        // setting up mock test
        request = mock(Request.class);
        response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);
        gameCenter= mock (GameCenter.class);
        playerLobby = mock(PlayerLobby.class);
        HttpSession = mock(Session.class);
        player=mock(Player.class);
        arrayList = mock(ArrayList.class);
        engine =  mock(TemplateEngine.class);

        home = new GetHomeRoute(engine,playerLobby,gameCenter);
    }

    /**
     * Test the home route
     */
    @Test
    public void testHomeRoute(){
        final  TemplateEngineTester testHelper= new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //final Session session = mock(Request.class).session();
        //final Response response = mock(Response.class);
        Mockito.when(request.session()).thenReturn(HttpSession);
        when(HttpSession.attribute(PostSignInRoute.PLAYER_KEY)).thenReturn(player);
        when(playerLobby.getPlayers()).thenReturn(new ArrayList<Player>());
        when(player.getName()).thenReturn("");
        //when asked for a game return mocked game
        when(gameCenter.getGame("")).thenReturn(mock(Game.class));
        final ModelAndView capt =mock(ModelAndView.class);
       // Map<String, Object> vm = new HashMap<String, Object>();

        Object s = home.handle(request,response);

        //analyze result
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        // run the test
        assertEquals("Welcome!", home.TITLE);



    }



    }


