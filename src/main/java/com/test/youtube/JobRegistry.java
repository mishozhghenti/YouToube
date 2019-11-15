package com.test.youtube;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class JobRegistry {

    ConcurrentMap<String, Job> jobs = new ConcurrentHashMap<>();

    public void postJob(Job job) {
        jobs.putIfAbsent(job.getUser().getUserName(), job);
    }

    public List<Job> getJobs() {
        return new ArrayList<>(jobs.values());
    }

    public Job removeJob(String user) {
        return jobs.remove(user);
    }

    public Job getJobForUser(String userId) {
        return jobs.get(userId);
    }
}
