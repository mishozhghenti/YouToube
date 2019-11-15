package com.test.youtube;

import com.test.youtube.repository.DataHelper;
import com.test.youtube.repository.UserHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Application implements AsyncConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("main-async-");
        threadPoolTaskExecutor.setCorePoolSize(16);
        return threadPoolTaskExecutor;
    }

    @Bean
    public UserHelper getUserFileWriter() throws IOException {
        return new UserHelper("./users.txt");
    }

    @Bean
    public DataHelper getUserDataFileWriter() throws IOException {
        return new DataHelper("./user_data.txt");
    }
}
