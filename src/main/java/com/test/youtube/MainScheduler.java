package com.test.youtube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MainScheduler {
    @Autowired
    JobRegistry jobRegistry;
    @Autowired
    AsyncJobRunner asyncJobRunner;

    @Scheduled(fixedDelay = 100)
    public void process(){
        for (Job job : jobRegistry.getJobs()) {
            asyncJobRunner.run(job);
        }
    }
}
