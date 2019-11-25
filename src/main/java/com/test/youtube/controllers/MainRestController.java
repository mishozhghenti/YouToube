package com.test.youtube.controllers;

import com.test.youtube.Job;
import com.test.youtube.JobRegistry;
import com.test.youtube.model.User;
import com.test.youtube.repository.DataHelper;
import com.test.youtube.repository.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class MainRestController {
    @Autowired
    JobRegistry jobRegistry;
    @Autowired
    SimpMessagingTemplate socket;
    @Autowired
    DataHelper dataHelper;
    @Autowired
    UserHelper userHelper;

    @Autowired
    @Qualifier("googleApiToken")
    String googleApiToken;

    @GetMapping("/startJob")
    public String startJob(HttpServletRequest req) {
        User currentUser = (User) req.getSession().getAttribute("user");
        jobRegistry.postJob(new Job(currentUser, socket, dataHelper, userHelper, googleApiToken));
        return "OK";
    }

    @PostMapping("/changeParams")
    public String editJob(HttpServletRequest req, long rate, String region) {
        User currentUser = (User) req.getSession().getAttribute("user");
        String userName = currentUser.getUserName();

        Job job = jobRegistry.getJobForUser(userName);
        if (job != null) {
            currentUser.setRate(rate * 1000);
            currentUser.setRegion(region);
            job.changeParams(currentUser);
        }
        return "OK";
    }

}
