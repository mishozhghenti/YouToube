package com.test.mihsoproject;

import com.test.youtube.Application;
import com.test.youtube.model.User;
import com.test.youtube.repository.DataHelper;
import com.test.youtube.repository.UserHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;

@SpringBootTest(classes = {Application.class})
class MihsoprojectApplicationTests {

    @Autowired
    UserHelper userHelper;
    @Autowired
    DataHelper dataHelper;
    private HashMap<String, User> allUsers;

    @Test
    void contextLoads() {

    }

    @Test
    void addNewUser() throws IOException {
        User newUser1 = new User("test1", 3000, "GE", "pass");
        User newUser2 = new User("test2", 4000, "GE", "password");
        userHelper.addNewUser(newUser1);
        allUsers = userHelper.getUsers();
        assert (allUsers.get(newUser1.getUserName()).equals(newUser1));
        userHelper.addNewUser(newUser2);
        allUsers = userHelper.getUsers();
        assert (allUsers.get(newUser2.getUserName()).equals(newUser2));
    }

    @Test
    void editUser() throws IOException {
        User newUser = new User("testuser", 3000, "GE", "pass");
        userHelper.addNewUser(newUser);
        newUser.setRate(10000);
        userHelper.editUser(newUser);
        allUsers = userHelper.getUsers();
        assert (allUsers.get(newUser.getUserName()).equals(newUser));
    }

    @Test
    void loginTest() throws IOException {
        User newUser = new User("testuser_login", 3000, "GE", "1234");
        userHelper.addNewUser(newUser);
        User loggedInUser = userHelper.logIn(newUser.getUserName(), newUser.getPassword());
        assert (loggedInUser.equals(newUser));
        loggedInUser = userHelper.logIn("RANDOM_STRING", "RANDOM_PASS");
        assert (loggedInUser == null);
    }

}
