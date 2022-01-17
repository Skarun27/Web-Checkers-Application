package com.webcheckers.ui;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class GetSignInRouteTest {

    private Request request;
    private Response response;
    private Session session;
    private TemplateEngine templateEngine;
    // component under test
    private GetSignInRoute CuT;

    // define all the mock objects and the CuT
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        templateEngine = mock(TemplateEngine.class);
        when(request.session()).thenReturn(session);
        CuT = new GetSignInRoute(templateEngine);
    }

    @Test
    void handleTest() {
        final TemplateEngineTester templateEngineTester = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(templateEngineTester.makeAnswer());

        // test request
        CuT.handle(request, response);
        // analyze results
        templateEngineTester.assertViewModelExists();
        templateEngineTester.assertViewName("signin.ftl");
        templateEngineTester.assertViewModelIsaMap();
    }
}