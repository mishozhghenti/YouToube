package com.test.youtube;

import com.test.youtube.model.Record;
import com.test.youtube.model.User;
import com.test.youtube.repository.DataHelper;
import com.test.youtube.repository.UserHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class Job {
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    DataHelper dataHelper;
    UserHelper userHelper;
    List<String> history = new ArrayList<>();
    private SimpMessagingTemplate socket;
    String jobId;
    long lastRunTime = 0;
    private User user;


    public Job(User user, SimpMessagingTemplate socket, DataHelper dataHelper, UserHelper userHelper) {
        this.jobId = UUID.randomUUID().toString();
        this.user = user;
        this.socket = socket;
        this.dataHelper = dataHelper;
        this.userHelper = userHelper;
    }

    private String process() throws IOException {
        Record newRecord = new Record("NULL", this.getUser().getUserName(), "NULL", "NULL");
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject("https://www.googleapis.com/youtube/v3/videos?part=contentDetails&chart=mostPopular&regionCode=" + this.getUser().getRegion() + "&key=AIzaSyDZUcUiWSb2Kk2P3qIPxjMxxlnYnZyI7RQ", String.class);
            JSONObject userJSON = new JSONObject(response);
            JSONArray jsonArray = (JSONArray) userJSON.get("items");
            JSONObject currentVideoDetails = (JSONObject) jsonArray.get(0);
            String videoID = currentVideoDetails.getString("id");
            newRecord = new Record(UUID.randomUUID().toString(), this.getUser().getUserName(), "https://www.youtube.com/embed/" + videoID, "$COMMENT_URL$");
            dataHelper.addData(newRecord);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return newRecord.toString();
    }

    public Optional<String> processRunner() {
        Optional<String> res = Optional.empty();
        if (System.currentTimeMillis() - lastRunTime > this.getUser().getRate().get()) {
            lastRunTime = System.currentTimeMillis();
            try {
                res = Optional.of(process());
                socket.convertAndSend("/topic/" + user.getUserName(), res.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public void changeParams(User updated) {
        try {
            this.userHelper.addNewUser(updated); // saves to file==db
            this.getUser().setRate(updated.getRate().get());
            this.getUser().setRegion(updated.getRegion());
        } catch (Exception e) {
        }
    }
}
