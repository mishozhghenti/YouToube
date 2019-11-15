package com.test.youtube.controllers;

import com.test.youtube.model.Record;
import com.test.youtube.model.User;
import com.test.youtube.repository.DataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserDataController {
    @Autowired
    DataHelper dataHelper;

    @GetMapping(value = "/userInfo")
    @ResponseBody
    public User getUserInfo(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }

    @GetMapping(value = "/userHistory")
    @ResponseBody
    public List<Record> getUserData(HttpServletRequest req) {
        List<Record> records = new ArrayList<>();
        try {
            records = dataHelper.getUserData(((User) req.getSession().getAttribute("user")).getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
