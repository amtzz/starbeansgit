package com.example.application.data.service;

import com.example.application.data.entity.UserData;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Service;

@Service
@VaadinSessionScope
public class SessionService {
    private UserData user;

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}
