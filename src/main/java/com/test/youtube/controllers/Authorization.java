package com.test.youtube.controllers;

import com.test.youtube.model.User;
import com.test.youtube.repository.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class Authorization {

    @Autowired
    UserHelper userFileHelper;

    @PostMapping(value = "/login")
    public String login(HttpServletRequest req, @RequestParam String username, @RequestParam String password) {
        String res = "-1";
        try {
            User user = userFileHelper.logIn(username, password);
            if (user != null) {
                res = "0";
                setUserToSession(user, req);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping(value = "/signup")
    public String signUp(HttpServletRequest req, @RequestParam String username, @RequestParam String region, @RequestParam long rate, @RequestParam String password) {
        User newUser = new User(username, rate * 1000, region, password);
        try {
            userFileHelper.addNewUser(newUser);
            setUserToSession(newUser, req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    private void setUserToSession(User user, HttpServletRequest req) {
        req.getSession().setAttribute("user", user);
    }
}
