package com.test.youtube;

import com.test.youtube.model.User;
import com.test.youtube.repository.DataHelper;
import com.test.youtube.repository.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MainRunner implements ApplicationRunner {
    @Autowired
    JobRegistry jobRegistry;
    @Autowired
    UserHelper userHelper;
    @Autowired
    SimpMessagingTemplate socket;
    @Autowired
    DataHelper dataHelper;


    /**
     * When application is loaded this method gets all users and starts jobs one by one
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        HashMap<String, User> allUsers = userHelper.getUsers();

      /*  for (String username : allUsers.keySet()) {
            jobRegistry.postJob(new Job(allUsers.get(username), socket, dataHelper, userHelper));
        }*/
    }
}
