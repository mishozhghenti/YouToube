package com.test.youtube;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class AsyncJobRunner {
    @Async
    public void run(Job job) {
        job.processRunner().ifPresent(s -> log.info("{}:{}", job.getUser().getUserName(), s));
    }
}
