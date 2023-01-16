package com.example.application.views.helloworld;

import com.example.application.data.service.SessionService;
import com.example.application.data.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Log In")
@Route(value = "login")
@RouteAlias(value = "")
public class LogInView extends HorizontalLayout {

    private final LoginForm loginForm;

    private final SessionService sessionService;
    private final UserService userService;

    public LogInView(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;

        this.loginForm = new LoginForm(sessionService, userService);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.CENTER, loginForm);

        add(loginForm);
    }

}
