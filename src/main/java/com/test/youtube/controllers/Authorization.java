package com.test.youtube.controllers;

import com.test.youtube.model.User;
import com.test.youtube.repository.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                req.getSession().setAttribute("user", user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String signUp() {
        return null;
    }

}
