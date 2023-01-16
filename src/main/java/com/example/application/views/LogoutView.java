package com.example.application.views;

import com.example.application.data.service.SessionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Logout")
@Route(value = "logout")
@Uses(Icon.class)
public class LogoutView extends Div {
    private final SessionService sessionService;

    public LogoutView(SessionService sessionService) {
        this.sessionService = sessionService;

        UI.getCurrent().getSession().getSession().invalidate();
        UI.getCurrent().getPage().setLocation("/");
    }
}
